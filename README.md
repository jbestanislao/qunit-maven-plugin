#QUnit Maven Plugin

Welcome to the qunit maven plugin.
This plugin has been designed to make qunit integrate in maven build as easy as possible.

Click [this](http://qunitjs.com) for a quick QUnit jumpstart.
### Prerequisites
1. Need to download the latest [qunit-xxx.js](http://code.jquery.com/qunit/qunit-1.10.0.js) and [qunit-xxx.css](http://code.jquery.com/qunit/qunit-1.10.0.css).

2. Put it inside src/main/test/javascript directory.

### How to use Qunit maven plugin

1. Where to put the test file(s).

    Place your test html to src/main/test/javascript (default source test directory).

2. Configuring maven build.

    ```
        <build>
            <plugins>
                <plugin>
                    <groupId>ph.restanislao.qunit</groupId>
                    <artifactId>qunit-maven-plugin</artifactId>
                    <version>1.0</version>
                    <executions>
                      <execution>
                        <phase>test</phase>
                        <goals>
                          <goal>runTest</goal>
                        </goals>
                      </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    ```

### Quick look

Most of the times it is easy to see the sample implementation.

See the [sample usage](https://github.com/jbestanislao/qunit-maven-plugin-example) of Qunit maven plugin.




