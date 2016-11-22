/**
* @author ZhangHongdong
* @date 2015年9月8日-上午9:28:00
* @see (参阅)
*/
package cn.signit.untils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.signit.untils.file.FileSeparator;

/**
 *class类查找器
 * @ClassName ClassSearcher
 * @author ZhangHongdong
 * @date 2015年9月8日-上午9:28:00
 * @version 1.1.0
 */
public class ClassSearcher {
		private Class<?>[] classArray;
		private String basePackage;
		private String packagePath;
		
		public ClassSearcher(String basePackage){
			this.basePackage = basePackage;
			this.packagePath = basePackage.replace('.', FileSeparator.AUTO_CHAR);
		}
	
		public Class<?>[] getClassArray() {
			if(classArray == null){
				classArray = new Class<?>[getClasses().size()];
				return getClasses().toArray(classArray);
			}else{
				return classArray;
			}
		}
		
		public Class<?> getClassByName(String className){
			for (Class<?> clazz: getClassArray()) {
				if(clazz.getName().endsWith(className)){
					return clazz;
				}
			}
			return null;
		} 
		
		public Class<?>[] getClassArrayByNames(String... classNames){
			List<Class<?>> classList = new ArrayList<>();
			for (Class<?> clazz: getClassArray()) {
				for(String className : classNames){
					if(clazz.getName().toLowerCase().endsWith(className.toLowerCase())){
						classList.add(clazz);
						break;
					}
				}
			}
			Class<?>[] clazzArr = new Class<?>[classList.size()];
			return classList.toArray(clazzArr);
		} 

		public void setClassArray(Class<?>[] classArray) {
			this.classArray = classArray;
		}

		public String getBasePackage() {
			return basePackage;
		}
		public void setBasePackage(String basePackage) {
			this.basePackage = basePackage;
		}
		public String getPackagePath() {
			return packagePath;
		}
		public void setPackagePath(String packagePath) {
			this.packagePath = packagePath;
		}
		
		private Set<Class<?>> getClasses(){
			// 第一个class类的集合  
	        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();  
	        // 是否循环迭代  
	        boolean recursive = true;  
	        // 定义一个枚举的集合 并进行循环来处理这个目录下的things  
	        Enumeration<URL> dirs;  
	        try {  
	            dirs = Thread.currentThread().getContextClassLoader().getResources(  
	            		getPackagePath());  
	            // 循环迭代下去  
	            while (dirs.hasMoreElements()) {  
	                // 获取下一个元素  
	                URL url = dirs.nextElement();  
	                // 得到协议的名称  
	                String protocol = url.getProtocol();  
	                // 如果是以文件的形式保存在服务器上  
	                if ("file".equals(protocol)) {  
	                    System.err.println("==============>> file类型的扫描");  
	                    // 获取包的物理路径  
	                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");  
	                    // 以文件的方式扫描整个包下的文件 并添加到集合中  
	                    findAndAddClassesInPackageByFile(getBasePackage(), filePath,  
	                            recursive, classes);  
	                } else if ("jar".equals(protocol)) {  
	                    // 如果是jar包文件  
	                    // 定义一个JarFile  
	                    System.err.println("==============>> jar类型的扫描");  
	                    JarFile jar;  
	                    try {  
	                        // 获取jar  
	                        jar = ((JarURLConnection) url.openConnection())  
	                                .getJarFile();  
	                        // 从此jar包 得到一个枚举类  
	                        Enumeration<JarEntry> entries = jar.entries();  
	                        // 同样的进行循环迭代  
	                        while (entries.hasMoreElements()) {  
	                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件  
	                            JarEntry entry = entries.nextElement();  
	                            String name = entry.getName();  
	                            // 如果是以/开头的  
	                            if (name.charAt(0) == '/') {  
	                                // 获取后面的字符串  
	                                name = name.substring(1);  
	                            }  
	                            // 如果前半部分和定义的包名相同  
	                            if (name.startsWith(getPackagePath())) {  
	                                int idx = name.lastIndexOf('/');  
	                                // 如果以"/"结尾 是一个包  
	                                if (idx != -1) {  
	                                    // 获取包名 把"/"替换成"."  
	                                    setBasePackage(name.substring(0, idx)  
	    	                                            .replace('/', '.'));
	                                }  
	                                // 如果可以迭代下去 并且是一个包  
	                                if ((idx != -1) || recursive) {  
	                                    // 如果是一个.class文件 而且不是目录  
	                                    if (name.endsWith(".class")  
	                                            && !entry.isDirectory()) {  
	                                        // 去掉后面的".class" 获取真正的类名  
	                                        String className = name.substring(  
	                                                getBasePackage().length() + 1, name  
	                                                        .length() - 6);  
	                                        try {  
	                                            // 添加到classes  
	                                            classes.add(Class  
	                                                    .forName(getBasePackage() + '.'  
	                                                            + className));  
	                                        } catch (ClassNotFoundException e) {  
	                                            // log  
	                                            // .error("添加用户自定义视图类错误 找不到此类的.class文件");  
	                                            e.printStackTrace();  
	                                        }  
	                                    }  
	                                }  
	                            }  
	                        }  
	                    } catch (IOException e) {  
	                        // log.error("在扫描用户定义视图时从jar包获取文件出错");  
	                        e.printStackTrace();  
	                    }  
	                }  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	  
	        return classes;  
		}
		
	    /** 
         * 以文件的形式来获取包下的所有Class 
         *  
         * @param packageName 
         * @param packagePath 
         * @param recursive 
         * @param classes 
         */  
        public void findAndAddClassesInPackageByFile(String packageName,  
                String packagePath, final boolean recursive, Set<Class<?>> classes) {  
            // 获取此包的目录 建立一个File  
            File dir = new File(packagePath);  
            // 如果不存在或者 也不是目录就直接返回  
            if (!dir.exists() || !dir.isDirectory()) {  
                // log.warn("用户定义包名 " + packageName + " 下没有任何文件");  
                return;  
            }  
            // 如果存在 就获取包下的所有文件 包括目录  
            File[] dirfiles = dir.listFiles(new FileFilter() {  
                // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)  
                public boolean accept(File file) {  
                    return (recursive && file.isDirectory())  
                            || (file.getName().endsWith(".class"));  
                }  
            });  
            // 循环所有文件  
            for (File file : dirfiles) {  
                // 如果是目录 则继续扫描  
                if (file.isDirectory()) {  
                    findAndAddClassesInPackageByFile(packageName + "."  
                            + file.getName(), file.getAbsolutePath(), recursive,  
                            classes);  
                } else {  
                    // 如果是java类文件 去掉后面的.class 只留下类名  
                    String className = file.getName().substring(0,  
                            file.getName().length() - 6);  
                    try {  
                        // 添加到集合中去  
                                            classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));    
                                    } catch (ClassNotFoundException e) {  
                        // log.error("添加用户自定义视图类错误 找不到此类的.class文件");  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
}
