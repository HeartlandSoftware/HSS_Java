<a href="https://github.com/HeartlandSoftware/HSS_Java/blob/master/LICENSE">

<img align="right" src="https://img.shields.io/badge/license-Apache 2-blue.svg"/>

</a>

# HSS_Java

Some of the features of this library:

* A unit conversion utility.

* A linked list implementation. Unlike the Java [LinkedList](https://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html) class this version gives the nodes access to their sibling nodes.

* General Java helper classes such as OutVariable which can be used to return values through method parameters. This can greatly simplify porting C++ applications to Java.

* Several [String](https://docs.oracle.com/javase/7/docs/api/java/lang/String.html) helper methods aimed at simplifying porting C++ code to Java.

#### Platform Support

Eclipse project files are included as well as an Ant build script. It requires at least JRE7.

## Examples

```java
//Converting a temperature from Celsius to Fahrenheit
double tempC = 25.0;
double tempF = Convert.ConvertUnit(tempC, STORAGE_FORMAT.FAHRENHEIT, STORAGE_FORMAT.CELSIUS);
```

## Support

Please use GitHub issues and Pull Requests for support.

## Copyright and license

Copyright 2016-2018 Heartland Software Solutions Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
