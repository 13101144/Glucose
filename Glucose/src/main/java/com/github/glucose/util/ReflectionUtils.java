package com.github.glucose.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Scope;

import com.github.glucose.annotation.Optional;
import com.github.glucose.core.DependencyModel;


/**
 * 反射工具类
 * @author yu
 *
 */
public abstract class ReflectionUtils {
	
	private final static Object lock = new Object();

    // We don't want to hold onto these classes if they are released by others
    private static final Map<Class<?>, LinkedHashSet<MemberKey>> methodKeyCache = new WeakHashMap<Class<?>, LinkedHashSet<MemberKey>>();
    private static Map<Class<?>, LinkedHashSet<MemberKey>> fieldCache = new WeakHashMap<Class<?>, LinkedHashSet<MemberKey>>();
    private final static Map<Class<?>, Method> postConstructCache = new WeakHashMap<Class<?>, Method>();
    private final static Map<Class<?>, Method> preDestroyCache = new WeakHashMap<Class<?>, Method>();
	private final static String CONVENTION_POST_CONSTRUCT = "postConstruct";
    private final static String CONVENTION_PRE_DESTROY = "preDestroy";
	
	public static Field[] getDeclaredFields(final Class<?> clazz) {
		return AccessController.doPrivileged(new PrivilegedAction<Field[]>() {

            public Field[] run() {
                return clazz.getDeclaredFields();
            }
            
        });
	}
	
	public static Method[] getDeclaredMethods(final Class<?> clazz) {
        return AccessController.doPrivileged(new PrivilegedAction<Method[]>() {

            public Method[] run() {
                return clazz.getDeclaredMethods();
            }
            
        });    
    }
	
	public static Constructor<?>[] getConstructorMethods(final Class<?> clazz) {
        return AccessController.doPrivileged(new PrivilegedAction<Constructor<?>[]>() {

            public Constructor<?>[] run() {
                return clazz.getConstructors();
            }
            
        });    
    }
	
	public static void setAccessible(final AccessibleObject ao) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                ao.setAccessible(true);
                return null;
            }
            
        });
	}
	
	public static List<DependencyModel> getConstructorDependencyModel(Constructor<?> c) {
		Type genericTypeParams[] = c.getGenericParameterTypes();
		Annotation paramAnnotations[][] = c.getParameterAnnotations();
		
		List<DependencyModel> result = new LinkedList<DependencyModel>();
		
		for(int i = 0; i < genericTypeParams.length; i++) {
			result.add(new DependencyModel(genericTypeParams[i],
					getAllQualifiers(paramAnnotations[i]), 
					i, 
					c.getDeclaringClass(), 
					isOptional(c.getAnnotations())));
		}
		
		return result;
	}
	
	public static List<DependencyModel> getMethodDependencyModel(Method m) { 
		Type genericTypeParams[] = m.getGenericParameterTypes();
		Annotation paramAnnotations[][] = m.getParameterAnnotations();
		
		List<DependencyModel> result = new LinkedList<DependencyModel>();
		
		for(int i = 0; i < genericTypeParams.length; i++) {
			result.add(new DependencyModel(genericTypeParams[i],
					getAllQualifiers(paramAnnotations[i]), 
					i, 
					m.getDeclaringClass(), 
					isOptional(m.getAnnotations())));
		}
		
		return result;
	}
	
	public static List<DependencyModel> getFiledDependencyModel(Field f) {
		List<DependencyModel> result = new LinkedList<DependencyModel>();
		result.add(new DependencyModel(f.getGenericType(),
				getAllQualifiers(f),
				-1,
				f.getDeclaringClass(),
				isOptional(f.getAnnotations())));
		return result;
	}
	
	private static boolean isContainInject(AnnotatedElement annotatedGuy) {
		Annotation[] annos = annotatedGuy.getAnnotations();
		if(annos == null) {
			return false;
		}
		return isInject(annos);
	}
	
	

	private static boolean isAnnotationAQualifier(Annotation anno) {
		Class<? extends Annotation> annoType = anno.annotationType();
		return annoType.isAnnotationPresent(Qualifier.class);
	}
	
	private static boolean isOptional(Annotation[] annotations) {
		for(Annotation anno : annotations) {
			if(anno.annotationType().isAnnotationPresent(Optional.class)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isInject(Annotation[] annotations) {
		for(Annotation anno : annotations) {
			if(anno.annotationType().isAnnotationPresent(Inject.class)) {
				return true;
			}
		}
		return false;
	}
	
	private static Set<Annotation> getAllQualifiers(AnnotatedElement annotatedGuy) {
		HashSet<Annotation> result = new HashSet<Annotation>();
		for(Annotation annotation : annotatedGuy.getAnnotations()) {
			if(isAnnotationAQualifier(annotation)) {
				result.add(annotation);
			}
		}
		return result;
	}
	
	
	private static Set<Annotation> getAllQualifiers(
            Annotation[] memberAnnotations) {
		HashSet<Annotation> result = new HashSet<Annotation>();
		for(Annotation annotation : memberAnnotations) {
			if(isAnnotationAQualifier(annotation)) {
				result.add(annotation);
			}
		}
		return result;
	}
	
	/**
	 * 获取原始类
	 * @param type
	 * @return
	 */
	public static Class<?> getRawClass(Type type) {
		if(type == null) return null;
		
		if(type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType)type).getGenericComponentType();
			
			if(!(componentType instanceof ParameterizedType)) {
				return null;
			}
			
			Class<?> rawComponentClass = getRawClass(componentType);
			
			String forNameName = "[L" + rawComponentClass.getName() + ";";
            try {
                return Class.forName(forNameName);
            }
            catch (Throwable th) {
                // ignore, but return null
                return null;
            }
		}
		
		if(type instanceof Class) {
			return (Class<?>) type;
		}
		
		if(type instanceof ParameterizedType) {
			Type rawType = ((ParameterizedType)type).getRawType();
			if (rawType instanceof Class) {
                return (Class<?>) rawType;
            }
		}
		
		return null;
	}
	
	/**
	 * 获取类的所有构造器
	 * @param clazz
	 * @return
	 */
	public static Set<Constructor<?>> getAllConstructors(Class<?> clazz) {
		HashSet<Constructor<?>> result = new HashSet<Constructor<?>>();
		
		HashSet<MemberKey> keys = new HashSet<MemberKey>();
		
		getAllConstructKeys(clazz,keys);
		
		for (MemberKey key : keys) {
			result.add((Constructor<?>) key.getBackingMember());
        }
        
        return result;
	}
	
	private static void getAllConstructKeys(Class<?> clazz, Set<MemberKey>
		currentConstructors) {
		if(clazz == null) return;
		
		for(Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			currentConstructors.add(new MemberKey(constructor));
		}
	}
	
	/**
	 * 获得类的所有方法
	 * @param clazz
	 * @return
	 */
	public static Set<Method> getAllMethods(Class<?> clazz) {
		HashSet<Method> result = new HashSet<Method>();
		
		HashSet<MemberKey> keys = new HashSet<MemberKey>();
		
		getAllMethodKeys(clazz,keys);
		
		for (MemberKey key : keys) {
			result.add((Method) key.getBackingMember());
        }
        
        return result;
	}
	
	private static void getAllMethodKeys(Class<?> clazz, Set<MemberKey> currentMethods) {
		if(clazz == null) return;
		
		getAllMethodKeys(clazz.getSuperclass(), currentMethods);
		
		for(Method method : getDeclaredMethods(clazz)) {
			currentMethods.add(new MemberKey(method));
		}
	}
	
	/**
	 * 获得类的所有域
	 * @param clazz
	 * @return
	 */
	public static Set<Field> getAllFields(Class<?> clazz) {
		HashSet<Field> result = new HashSet<Field>();
		
		HashSet<MemberKey> keys = new HashSet<MemberKey>();
		
		getAllFieldKeys(clazz,keys);
		
		for (MemberKey key : keys) {
			result.add((Field) key.getBackingMember());
        }
        
        return result;
	}
	
	private static void getAllFieldKeys(Class<?> clazz, Set<MemberKey> currentFields) {
		if(clazz == null) return;
		
		getAllMethodKeys(clazz.getSuperclass(), currentFields);
		
		for(Field field : getDeclaredFields(clazz)) {
			currentFields.add(new MemberKey(field));
		}
	}
	
	/**
	 * 获得类的所有注入方法
	 * @param annotateType
	 * @param errorCollector
	 * @return
	 */
	public static Set<Method> findInjectMethods(Class<?> annotateType,
			ExceptionCollector errorCollector) {
		HashSet<Method> result = new HashSet<Method>();
		
		for(Method method : getAllMethods(annotateType)) {
			if(!isContainInject(method)) {
				continue;
			}
			
			if(!hasCorrectInjectMethodModifiers(method)) {
				errorCollector.addThrowable(new IllegalArgumentException("The method " +
		                  " may not be static or final"));
		                continue;
			}
			
			result.add(method);
		}
		
		return result;
	}
	
	/**
	 * 获得类的所有注入方法
	 * @param annotateType
	 * @param errorCollector
	 * @return
	 */
	public static Set<Field> findInjectFields(Class<?> annotateType,
			ExceptionCollector errorCollector) {
		HashSet<Field> result = new HashSet<Field>();
		
		for(Field field : getAllFields(annotateType)) {
			if(!isContainInject(field)) {
				continue;
			}
			
			if(!hasCorrectInjectFieldModifiers(field)) {
				errorCollector.addThrowable(new IllegalArgumentException("The method " +
		                  " may not be static or final"));
		                continue;
			}
			
			result.add(field);
		}
		
		return result;
	}
	
	/**
	 * 获取类的构造函数，注入的构造函数仅且只能有一个，或者无参数构造器
	 * @param member
	 * @return
	 */
	public static Constructor<?> getChosenConstructor(Class<?> annotateType,
			ExceptionCollector errorCollector) {
		Constructor<?> zeroArgConstructor = null;
		Constructor<?> injectAnnotationConstructor = null;
		
		for(Constructor<?> constructor : getAllConstructors(annotateType)) {
			if(isContainInject(constructor)){
				if(injectAnnotationConstructor != null) {
					errorCollector.addThrowable(new IllegalArgumentException("There is more than one constructor on class"));
				}
				injectAnnotationConstructor = constructor;
			}
			
			if(constructor.getParameterCount() == 0) {
				zeroArgConstructor = constructor;
			}
		}
		
		if(injectAnnotationConstructor == null) {
			return zeroArgConstructor;
		}
		
		return injectAnnotationConstructor;
	}
	
	private static boolean hasCorrectInjectMethodModifiers(Method member) {
        if (isStatic(member)) return false;
        if (isAbstract(member)) return false;
        
        return true;
    } 
	
	private static boolean hasCorrectInjectFieldModifiers(Field field) {
        if (isStatic(field)) return false;
        if (isFinal(field)) return false;
        
        return true;
    }
	
	public static boolean isStatic(Member member) {
        int modifiers = member.getModifiers();
        
        return ((modifiers & Modifier.STATIC) != 0);
    }
	
	public static boolean isAbstract(Member member) {
        int modifiers = member.getModifiers();
        
        return ((modifiers & Modifier.ABSTRACT) != 0);
    }
	
	public static boolean isFinal(Member member) {
		int modifiers = member.getModifiers();

		return ((modifiers & Modifier.FINAL) != 0);
	}
	
	private static class MemberKey {
        private final Member backingMember;
        
        private MemberKey(Member method) {
            backingMember = method;
        }
        
        private Member getBackingMember() {
            return backingMember;
        }
        
        public int hashCode() {
            int startCode = 0;
            if (backingMember instanceof Method) {
                startCode = 1;
            }
            else if (backingMember instanceof Constructor) {
                startCode = 2;
            }
            
            startCode ^= backingMember.getName().hashCode();
            
            Class<?> parameters[];
            if (backingMember instanceof Method) {
                parameters = ((Method) backingMember).getParameterTypes();
            }
            else if (backingMember instanceof Constructor) {
                parameters = ((Constructor<?>) backingMember).getParameterTypes();
            }
            else {
                parameters = new Class<?>[0];
            }
            
            for (Class<?> param : parameters) {
                startCode ^= param.hashCode();
            }
            
            return startCode;
        }
        
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof MemberKey)) return false;
            
            MemberKey omk = (MemberKey) o;
            
            Member oMember = omk.backingMember;
            
            if ((backingMember instanceof Method) && !(oMember instanceof Method)) {
                return false;
            }
            if ((backingMember instanceof Constructor) && !(oMember instanceof Constructor)) {
                return false;
            }
            
            if (!oMember.getName().equals(backingMember.getName())) return false;
            
            Class<?> oParams[];
            Class<?> bParams[];
            if (backingMember instanceof Method) {
                oParams = ((Method) oMember).getParameterTypes();
                bParams = ((Method) backingMember).getParameterTypes();
            }
            else if (backingMember instanceof Constructor) {
                oParams = ((Constructor<?>) oMember).getParameterTypes();
                bParams = ((Constructor<?>) backingMember).getParameterTypes();
            }
            else {
                oParams = new Class<?>[0];
                bParams = new Class<?>[0];
            }
            
            if (oParams.length != bParams.length) return false;
            for (int i = 0; i < oParams.length; i++) {
                if (oParams[i] != bParams[i]) return false;
            }
            
            return true;
        }
    }
	
	public static Set<Type> getAdvertisedTypesFromObject(Object t, Class<? extends Annotation> markerAnnotation) {
		if(t == null) return Collections.emptySet();
		
		return getAdvertisedTypesFromClass(t.getClass(), markerAnnotation);
	}
	
	public static Set<Type> getAdvertisedTypesFromClass(Type type, Class<? extends Annotation> markerAnnotation) {
		Set<Type> retVal = new LinkedHashSet<Type>();
		
		if(type == null) return retVal;
		
		Class<?> originalRawClass = getRawClass(type);
		
		if(originalRawClass == null) return retVal;
		
		Type genericSuperclass = originalRawClass.getGenericSuperclass();
		
		while (genericSuperclass != null) {
			Class<?> rawClass = getRawClass(genericSuperclass);
            if (rawClass == null) break;

            if (rawClass.isAnnotationPresent(markerAnnotation)) {
                retVal.add(genericSuperclass);
            }

            genericSuperclass = rawClass.getGenericSuperclass();
		}
		
		Set<Class<?>> alreadyHandled = new HashSet<Class<?>>();
        while (originalRawClass != null) {
            getAllContractsFromInterfaces(originalRawClass,
                markerAnnotation,
                retVal,
                alreadyHandled);

            originalRawClass = originalRawClass.getSuperclass();
        }

        return retVal;
		
	}
	
	private static void getAllContractsFromInterfaces(Class<?> clazzOrInterface,
            Class<? extends Annotation> markerAnnotation,
            Set<Type> addToMe,
            Set<Class<?>> alreadyHandled) {
        Type interfacesAsType[] = clazzOrInterface.getGenericInterfaces();

        for (Type interfaceAsType : interfacesAsType) {
            Class<?> interfaceAsClass = getRawClass(interfaceAsType);
            if (interfaceAsClass == null) continue;
            if (alreadyHandled.contains(interfaceAsClass)) continue;
            alreadyHandled.add(interfaceAsClass);

            if (interfaceAsClass.isAnnotationPresent(markerAnnotation)) {
                addToMe.add(interfaceAsType);
            }

            getAllContractsFromInterfaces(interfaceAsClass, markerAnnotation, addToMe, alreadyHandled);
        }
    }
	
	public static Annotation getScopeFromObject(Object t, Annotation annoDefault) {
        if (t == null) return annoDefault;

        return getScopeFromClass(t.getClass(), annoDefault);
    }
	
	public static Annotation getScopeFromClass(Class<?> clazz, Annotation annoDefault) {
        if (clazz == null) return annoDefault;

        for (Annotation annotation : clazz.getAnnotations()) {
            Class<? extends Annotation> annoClass = annotation.annotationType();

            if (annoClass.isAnnotationPresent(Scope.class)) {
                return annotation;
            }

        }

        return annoDefault;
    }
	
	public static String getName(Class<?> implClass) {
        Named named = implClass.getAnnotation(Named.class);

        String namedName = (named != null) ? getNamedName(named, implClass) : null ;

        if (namedName != null) return namedName;

        return null;
    }
	
	private static String getNamedName(Named named, Class<?> implClass) {
        String name = named.value();
        if (name != null && !name.equals("")) return name;

        String cn = implClass.getName();

        int index = cn.lastIndexOf(".");
        if (index < 0) return cn;

        return cn.substring(index + 1);
    }
	
	public static Set<Annotation> getQualifiersFromObject(Object t) {
        if (t == null) return Collections.emptySet();

        return getQualifierAnnotations(t.getClass());
    }
	
	private static Set<Annotation> internalGetQualifierAnnotations(AnnotatedElement annotatedGuy) {
        Set<Annotation> retVal = new LinkedHashSet<Annotation>();
        if (annotatedGuy == null) return retVal;

        for (Annotation annotation : annotatedGuy.getAnnotations()) {
            if (isAnnotationAQualifier(annotation)) {
                if ((annotatedGuy instanceof Field) &&
                        Named.class.equals(annotation.annotationType())) {
                    Named n = (Named) annotation;
                    if (n.value() == null || "".equals(n.value())) {
                        // Because we do not have access to AnnotationLiteral
                        // we cannot "fix" a Named annotation that has no explicit
                        // value here, and we must rely on the caller of this
                        // method to "fix" that case for us
                        continue;
                    }
                }
                retVal.add(annotation);
            }
        }

        if (!(annotatedGuy instanceof Class)) return retVal;

        Class<?> clazz = (Class<?>) annotatedGuy;
        while (clazz != null) {
            for (Class<?> iFace : clazz.getInterfaces()) {
                for (Annotation annotation : iFace.getAnnotations()) {
                    if (isAnnotationAQualifier(annotation)) {
                        retVal.add(annotation);
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }

        return retVal;

    }
	
	public static Set<Annotation> getQualifierAnnotations(final AnnotatedElement annotatedGuy) {
        Set<Annotation> retVal = AccessController.doPrivileged(new PrivilegedAction<Set<Annotation>>() {

            public Set<Annotation> run() {
                return internalGetQualifierAnnotations(annotatedGuy);
            }

        });

        return retVal;
    }
	
	public static Set<String> getContractsFromClass(Class<?> clazz, Class<? extends Annotation> markerAnnotation) {
        Set<String> retVal = new LinkedHashSet<String>();
        if (clazz == null) return retVal;

        retVal.add(clazz.getName());

        Class<?> extendsClasses = clazz.getSuperclass();
        while (extendsClasses != null) {
            if (extendsClasses.isAnnotationPresent(markerAnnotation)) {
                retVal.add(extendsClasses.getName());
            }

            extendsClasses = extendsClasses.getSuperclass();
        }

        while (clazz != null) {
            Class<?> interfaces[] = clazz.getInterfaces();
            for (Class<?> iFace : interfaces) {
                if (iFace.isAnnotationPresent(markerAnnotation)) {
                    retVal.add(iFace.getName());
                }
            }

            clazz = clazz.getSuperclass();
        }

        return retVal;
    }
	
	public static Set<String> getQualifiersFromClass(Class<?> clazz) {
        Set<String> retVal = new LinkedHashSet<String>();
        if (clazz == null) return retVal;

        for (Annotation annotation : clazz.getAnnotations()) {
            if (isAnnotationAQualifier(annotation)) {
                retVal.add(annotation.annotationType().getName());
            }

        }

        while (clazz != null) {
            for (Class<?> iFace : clazz.getInterfaces()) {
                for (Annotation annotation : iFace.getAnnotations()) {
                    if (isAnnotationAQualifier(annotation)) {
                        retVal.add(annotation.annotationType().getName());
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }

        return retVal;
    }

	public static Class<? extends Annotation> getScopeFromObject(Object t, Class<? extends Annotation> annoDefault){
		if (t == null) return annoDefault;

        return getScopeFromClass(t.getClass(), annoDefault);
	}
	
	public static Class<? extends Annotation> getScopeFromClass(Class<?> clazz, Class<? extends Annotation> annoDefault) {
        if (clazz == null) return annoDefault;
        
        for(Annotation annotation : clazz.getAnnotations()){
        	Class<? extends Annotation> annoClass = annotation.annotationType();
        	
        	if(annoClass.isAnnotationPresent(Scope.class)){
        		return annoClass;
        	}
        }
        
        return annoDefault;
	}
	
	public static Set<Method> findInitializerMethods(
            Class<?> annotatedType,
            ExceptionCollector errorCollector) {
        LinkedHashSet<Method> retVal = new LinkedHashSet<Method>();

        for (Method method : getAllMethods(annotatedType)) {
            

            if (!isProperMethod(method)) {
                errorCollector.addThrowable(new IllegalArgumentException(
                        "An initializer method " + Pretty.method(method) +
                                " is static, abstract or has a parameter that is an annotation"));
                continue;
            }

            retVal.add(method);
        }

        return retVal;
    }
	
	public static Set<Field> findInitializerFields(Class<?> annotatedType,ExceptionCollector errorCollector) {
		LinkedHashSet<Field> retVal = new LinkedHashSet<Field>();

		for (Field field : getAllFields(annotatedType)) {
			
			if (!isProperField(field)) {
				errorCollector
						.addThrowable(new IllegalArgumentException(
								"The field "
										+ Pretty.field(field)
										+ " may not be static, final or have an Annotation type"));
				continue;
			}

			retVal.add(field);
		}

		return retVal;
	}
	
	private static boolean isProperMethod(Method method) {
		if(isStatic(method)) return false;
		if(isAbstract(method)) return false;
		for(Class<?> paramClazz : method.getParameterTypes()) {
			if(paramClazz.isAnnotation()) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean isProperField(Field field) {
        if (isStatic(field)) return false;
        if (isFinal(field)) return false;
        Class<?> type = field.getType();
        return !type.isAnnotation();
    }
	
	public static Method findPostConstruct(Class<?> clazz, ExceptionCollector collector) {
		if (com.github.glucose.core.PostConstruct.class.isAssignableFrom(clazz)) {
			try {
				return clazz.getMethod(CONVENTION_POST_CONSTRUCT, new Class<?>[0]);
			} catch (NoSuchMethodException e) {
                return null;
            }
		}
		
		boolean containsKey;
		Method retVal;
		synchronized (lock) {
			containsKey = postConstructCache.containsKey(clazz);
			retVal = postConstructCache.get(clazz);
		}
		
		if(!containsKey){
			for (Method method : getDeclaredMethods(clazz)) {
	            boolean isPostConstruct = isPostConstruct(method);
	            if(isPostConstruct){
	            	retVal = method;
	            	postConstructCache.put(clazz, retVal);
	            }
			}
			
		}
		
		if (retVal == null) return null;
		if (retVal.isAnnotationPresent(PostConstruct.class) &&
                (retVal.getParameterTypes().length != 0)) {
            collector.addThrowable(new IllegalArgumentException("The method " + Pretty.method(retVal) +
                        " annotated with @PostConstruct must not have any arguments"));
            return null;
        }

        return retVal;
		
	}
	
	public static Method findPreDestroy(Class<?> clazz, ExceptionCollector collector) {
		if (com.github.glucose.core.PostConstruct.class.isAssignableFrom(clazz)) {
			try {
				return clazz.getMethod(CONVENTION_PRE_DESTROY, new Class<?>[0]);
			} catch (NoSuchMethodException e) {
                return null;
            }
		}
		
		boolean containsKey;
		Method retVal;
		synchronized (lock) {
			containsKey = preDestroyCache.containsKey(clazz);
			retVal = preDestroyCache.get(clazz);
		}
		
		if(!containsKey){
			for (Method method : getDeclaredMethods(clazz)) {
	            boolean isPreDestory = isPreDestroy(method);
	            if(isPreDestory){
	            	retVal = method;
	            	postConstructCache.put(clazz, retVal);
	            }
			}
			
		}
		
		if (retVal == null) return null;
		if (retVal.isAnnotationPresent(PostConstruct.class) &&
                (retVal.getParameterTypes().length != 0)) {
            collector.addThrowable(new IllegalArgumentException("The method " + Pretty.method(retVal) +
                        " annotated with @PostConstruct must not have any arguments"));
            return null;
        }

        return retVal;
		
	}
		
	private static boolean isPostConstruct(Method m) {
		if (m.isAnnotationPresent(PostConstruct.class))
			return true;

		if (m.getParameterTypes().length != 0)
			return false;
		return CONVENTION_POST_CONSTRUCT.equals(m.getName());
	}

	private static boolean isPreDestroy(Method m) {
		if (m.isAnnotationPresent(PreDestroy.class))
			return true;

		if (m.getParameterTypes().length != 0)
			return false;
		return CONVENTION_PRE_DESTROY.equals(m.getName());
	}
	
	
	
	
}
