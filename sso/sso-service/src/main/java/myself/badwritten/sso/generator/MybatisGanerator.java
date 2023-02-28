package myself.badwritten.sso.generator;


import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import myself.badwritten.common.util.StringUtils;


import java.util.Scanner;

/**
 * @className MybatisGanerator
 * @Description TODO
 * @Author RedLeaf
 * @Date 2022-1-9 21:44
 * @Version 1.0
 **/
public class MybatisGanerator {

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }


    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Veloctiy
//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        //D:\java\workspase\BadWritten\sso\src\main\java\myself\badwritten\sso
        String module = scanner("模块名");
        gc.setOutputDir(projectPath + "/" + module + "/src/main/java");  //生成路径(一般都是生成在此项目的src/main/java下面)
        gc.setAuthor("RedLeaf"); //设置作者
        gc.setOpen(false);
        gc.setFileOverride(true); //第二次生成会把第一次生成的覆盖掉
//        gc.setEntityName("%sEntity");			//实体命名方式  默认值：null 例如：%sEntity 生成 UserEntity
        gc.setMapperName("%sMapper");			//mapper 命名方式 默认值：null 例如：%sDao 生成 UserDao
        gc.setXmlName("%sMapper");				//Mapper xml 命名方式   默认值：null 例如：%sDao 生成 UserDao.xml
        gc.setServiceName("%sService");			//service 命名方式   默认值：null 例如：%sBusiness 生成 UserBusiness
        gc.setServiceImplName("%sServiceImpl");	//service impl 命名方式  默认值：null 例如：%sBusinessImpl 生成 UserBusinessImpl
        gc.setControllerName("%sController");	//controller 命名方式    默认值：null 例如：%sAction 生成 UserAction
        gc.setBaseResultMap(true); //生成resultMap
        mpg.setGlobalConfig(gc);

        //2、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/myself?useUnicode=true&serverTimezone=UTC&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 3、包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName("dao");
        pc.setParent("myself.badwritten." + module);
        pc.setMapper("dao");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // 4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);	//表名生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
//	        strategy.setCapitalMode(true);			// 全局大写命名 ORACLE 注意
//	        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");	//自定义继承的Entity类全称，带包名
//	        strategy.setSuperEntityColumns(new String[] { "test_id", "age" }); 	//自定义实体，公共字段
        strategy.setSuperServiceImplClass("myself.badwritten.common.base.BaseService");
        strategy.setSuperEntityClass("myself.badwritten.common.base.BaseModel");
        strategy.setSuperControllerClass("myself.badwritten.common.base.BaseController");
        strategy.setEntityLombokModel(true);	//【实体】是否为lombok模型（默认 false
        strategy.setRestControllerStyle(true);	//生成 @RestController 控制器
//	        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");	//自定义继承的Controller类全称，带包名
        strategy.setInclude("t_user","t_account");		//需要包含的表名，允许正则表达式（与exclude二选一配置）
        strategy.setTablePrefix("t_");		//表前缀
//	        strategy.setInclude(new String[] { "user" }); // 需要生成的表可以多张表
//	        strategy.setExclude(new String[]{"test"}); // 排除生成的表
        strategy.setControllerMappingHyphenStyle(true);	//驼峰转连字符
        strategy.entityTableFieldAnnotationEnable(true);	//是否生成实体时，生成字段注解
        mpg.setStrategy(strategy);

        //5、执行
        mpg.execute();

    }
}
