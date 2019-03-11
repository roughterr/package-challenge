# Package Packer

## Getting Started
The main entry point is the static method "pack" of class com.mobiquityinc.packer.Packer.
This method accepts the absolute path to a test file as a String. It does return the solution as a String.
## Deployment
To use Packer as a library in your project install it as a Maven library using the command: mvn install
And then add the dependency in your pom.xml:
<dependency>
  <groupId>package-challenge</groupId>
  <artifactId>package-challenge</artifactId>
  <version>1</version>
</dependency>