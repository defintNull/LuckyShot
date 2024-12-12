plugins {
    id("application")
}

application {
    mainClass = "org.luckyshot.Main"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.luckyshot.Main"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

group = "org.luckyshot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.hibernate:hibernate-core:6.6.3.Final")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.2")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.24.2")
    implementation("org.springframework.security:spring-security-crypto:5.8.0")
}

tasks.test {
    useJUnitPlatform()
}