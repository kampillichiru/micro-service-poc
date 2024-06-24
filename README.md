<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-release-plugin</artifactId>
                <version>3.0.0-M1</version>
                <configuration>
                    <tagNameFormat>@{project.version}</tagNameFormat>
                    <releaseProfiles>release</releaseProfiles>
                    <goals>clean deploy</goals>
                </configuration>
            </plugin>
        </plugins>
    </build>

