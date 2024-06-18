   <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-release-plugin</artifactId>
        <version>3.0.0-M1</version>
        <configuration>
          <tagNameFormat>@{project.version}</tagNameFormat>
          <goals>clean deploy</goals>
          <autoVersionSubmodules>true</autoVersionSubmodules>
        </configuration>
      </plugin>
