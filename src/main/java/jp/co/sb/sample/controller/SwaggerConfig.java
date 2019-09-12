package jp.co.sb.sample.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket iotmonitoring() {
		return getDocket("IoT Monitoring All", "/iotmonitoring");
    }
    
	@Bean
    public Docket user() {
    	return getDocket("IoT Monitoring User", "/user");
    }

	@Bean
    public Docket setting() {
    	return getDocket("IoT Monitoring Setting", "/setting");
    }
    
	@Bean
    public Docket notification() {
    	return getDocket("IoT Monitoring Notification", "/notification");
    }
    
	@Bean
    public Docket healthcheck() {
        return getDocket("IoT Monitoring Healthcheck", "/healthcheck");
    }

    @SuppressWarnings("unchecked")
	private Docket getDocket(String name, String path) {
        return new Docket(DocumentationType.SWAGGER_2)
        		.protocols(Collections.singleton("https"))
        		.host("mietell.sbgazar.com")
                .groupName(name)    // APIドキュメントをグルーピングするための識別名
                .select()
                .paths(Predicates.or(Predicates.containsPattern(path)))
                .build()
                .apiInfo(apiInfo());
    }
    
    private ApiInfo apiInfo() {
        // http://springfox.github.io/springfox/javadoc/2.6.0/index.html?springfox/documentation/service/ApiInfo.html
        return new ApiInfo(
                  "IoT Monitoring API"  // APIのタイトル
                , ""  // APIの説明
                , "V1"  // APIのバージョン
                , ""    // APIの利用規約へのURL
                , new Contact(
                         ""      // APIに関する連絡先組織・団体等
                        ,"" // APIに関する連絡先組織・団体等のWeb Site Url
                        ,""
                        )     // APIに関する連絡先組織・団体等のメールアドレス
                , "" // APIのライセンス
                , ""   // APIのライセンスURL
                , new ArrayList<VendorExtension>()  // 独自に拡張したいドキュメントがあればここで作成
        );
//        return new ApiInfo(
//                "IoT Monitoring API"  // APIのタイトル
//              , "このAPIは～～～～です"  // APIの説明
//              , "V1"  // APIのバージョン
//              , "????"    // APIの利用規約へのURL
//              , new Contact(
//                       "株式会社XXXXXXX"      // APIに関する連絡先組織・団体等
//                      ,"http://XXXXXXXXXXX.co.jp" // APIに関する連絡先組織・団体等のWeb Site Url
//                      ,"XXXXXXXX@example.jp"
//                      )     // APIに関する連絡先組織・団体等のメールアドレス
//              , "API LICENSE" // APIのライセンス
//              , "http://XXXXXXXXXXXX.co.jp"   // APIのライセンスURL
//              , new ArrayList<VendorExtension>()  // 独自に拡張したいドキュメントがあればここで作成
//      );
    }
}