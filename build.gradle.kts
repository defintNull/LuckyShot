plugins {
    id("application")
}

application {
    mainClass = "org.luckyshot.Main"
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
}

tasks.test {
    useJUnitPlatform()
}