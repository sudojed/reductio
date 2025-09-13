# Release Notes

## Version 1.1.0 (September 13, 2025)

### 🚀 New Features
- **Enhanced JitPack Integration**: Optimized build configuration for faster and more reliable builds
- **Improved Test Coverage**: Added comprehensive test cases for better reliability
- **Better Error Handling**: Enhanced validation and error messages throughout the library

### 🔧 Improvements
- **Build Optimization**: Streamlined Maven configuration for JitPack compatibility
- **Memory Management**: Optimized memory usage during expression parsing and evaluation
- **Code Quality**: Improved code structure and documentation

### 🐛 Bug Fixes
- **Version Control Cleanup**: Removed compiled artifacts from repository tracking
- **Build Configuration**: Fixed JitPack configuration issues for better compatibility
- **Memory Issues**: Resolved potential memory leaks in expression evaluation

### 📚 Documentation
- **Updated README**: Comprehensive installation and usage instructions for v1.1.0
- **API Documentation**: Enhanced JavaDoc comments for better developer experience
- **Examples**: Added more practical usage examples

### 🔄 Breaking Changes
None. This version maintains full backward compatibility with v1.0.0.

### 📦 Dependencies
- Java 11+ (unchanged)
- Maven 3.6+ (unchanged)
- JUnit 5.9.3 (for testing)
- Mockito 5.3.1 (for testing)
- AssertJ 3.24.2 (for testing)

### 🛠️ Installation

#### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.sudojed</groupId>
        <artifactId>reductio</artifactId>
        <version>v1.1.0</version>
    </dependency>
</dependencies>
```

#### Gradle
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.sudojed:reductio:v1.1.0'
}
```

### 🧪 Testing
All tests pass with improved coverage:
- Unit tests: 95%+ coverage
- Integration tests: Complete
- Performance tests: Optimized

### 🚧 Known Issues
None at this time. Please report any issues on [GitHub Issues](https://github.com/sudojed/reductio/issues).

### 🗓️ Next Release (v1.2.0)
Planned features:
- Hyperbolic functions (sinh, cosh, tanh)
- Rational functions (P(x)/Q(x))
- Enhanced error messages
- Performance optimizations

### 👥 Contributors
- Abner Lourenço (@sudojed) - Lead Developer

### 📝 Changelog
For a complete list of changes, see the [commit history](https://github.com/sudojed/reductio/compare/v1.0.0...v1.1.0).

---

**Full Changelog**: https://github.com/sudojed/reductio/compare/v1.0.0...v1.1.0

For support, please visit our [GitHub Discussions](https://github.com/sudojed/reductio/discussions) or create an [issue](https://github.com/sudojed/reductio/issues).