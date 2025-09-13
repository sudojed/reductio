# Publishing Guide - Reductio Library

This guide explains how to publish the Reductio library to GitHub Packages and maintain releases.

## Table of Contents

- [Prerequisites](#prerequisites)
- [GitHub Packages Setup](#github-packages-setup)
- [Publishing Process](#publishing-process)
- [Automated Publishing](#automated-publishing)
- [Manual Publishing](#manual-publishing)
- [Version Management](#version-management)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Before publishing, ensure you have:

1. **GitHub Account** with write access to the repository
2. **Personal Access Token** with `write:packages` and `repo` scopes
3. **Maven 3.6+** and **Java 11+** installed locally
4. **Git** configured with your GitHub credentials

## GitHub Packages Setup

### 1. Create Personal Access Token

1. Go to GitHub Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Click "Generate new token (classic)"
3. Select scopes:
   - `repo` (Full control of private repositories)
   - `write:packages` (Upload packages to GitHub Package Registry)
   - `read:packages` (Download packages from GitHub Package Registry)
4. Copy the generated token securely

### 2. Configure Maven Settings

Create or update `~/.m2/settings.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                             http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

### 3. Set Environment Variables

```bash
export GITHUB_USERNAME=your-username
export GITHUB_TOKEN=your-personal-access-token
```

## Publishing Process

### Option 1: Automated Publishing (Recommended)

The repository includes GitHub Actions workflows that automatically publish when you create a new tag.

#### Steps:

1. **Update Version:**
   ```bash
   mvn versions:set -DnewVersion=1.2.0 -DgenerateBackupPoms=false
   git add pom.xml
   git commit -m "chore: bump version to 1.2.0"
   ```

2. **Create and Push Tag:**
   ```bash
   git tag -a v1.2.0 -m "Release v1.2.0: Description of changes"
   git push origin v1.2.0
   ```

3. **Monitor Workflow:**
   - Go to GitHub Actions tab
   - Watch the "Publish to GitHub Packages" workflow
   - Check for successful completion

### Option 2: Manual Publishing

If you need to publish manually:

#### Steps:

1. **Clean and Test:**
   ```bash
   mvn clean test
   ```

2. **Build Artifacts:**
   ```bash
   mvn clean compile
   mvn source:jar javadoc:jar
   ```

3. **Deploy to GitHub Packages:**
   ```bash
   mvn deploy -DskipTests=true
   ```

## Automated Publishing

### GitHub Actions Workflows

The repository includes two main workflows:

#### 1. CI Workflow (`.github/workflows/ci.yml`)
- Triggers on push/PR to main branches
- Tests on multiple Java versions (11, 17, 21)
- Validates code quality
- Simulates JitPack build

#### 2. Publish Workflow (`.github/workflows/publish.yml`)
- Triggers on tag push or release
- Publishes to GitHub Packages
- Creates GitHub Release
- Uploads artifacts

### Workflow Triggers

The publish workflow triggers on:
- **Tag push:** `v*` or `[0-9]+.[0-9]+.[0-9]+` patterns
- **Release published:** When you create a release in GitHub UI
- **Manual dispatch:** You can trigger manually with custom version

### Manual Workflow Trigger

To trigger publishing manually:

1. Go to GitHub Actions → "Publish to GitHub Packages"
2. Click "Run workflow"
3. Optionally specify a version
4. Click "Run workflow"

## Version Management

### Semantic Versioning

Follow [Semantic Versioning](https://semver.org/):
- **MAJOR.MINOR.PATCH** (e.g., 1.2.3)
- **MAJOR:** Breaking changes
- **MINOR:** New features (backward compatible)
- **PATCH:** Bug fixes (backward compatible)

### Version Update Process

1. **Choose Version Number:**
   ```bash
   # For bug fixes
   mvn versions:set -DnewVersion=1.1.1 -DgenerateBackupPoms=false
   
   # For new features
   mvn versions:set -DnewVersion=1.2.0 -DgenerateBackupPoms=false
   
   # For breaking changes
   mvn versions:set -DnewVersion=2.0.0 -DgenerateBackupPoms=false
   ```

2. **Update Release Notes:**
   ```bash
   # Update RELEASE_NOTES.md with new version info
   git add RELEASE_NOTES.md pom.xml
   git commit -m "chore: prepare release v1.2.0"
   ```

3. **Create Tag:**
   ```bash
   git tag -a v1.2.0 -m "Release v1.2.0: Add new mathematical functions"
   git push origin v1.2.0
   ```

## Package Usage

### For Consumers

After publishing, users can add the dependency:

#### Maven
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/sudojed/reductio</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.sudojed</groupId>
        <artifactId>reductio</artifactId>
        <version>1.1.0</version>
    </dependency>
</dependencies>
```

#### Gradle
```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/sudojed/reductio")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation 'com.github.sudojed:reductio:1.1.0'
}
```

## Troubleshooting

### Common Issues

#### 1. Authentication Failed
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-deploy-plugin:2.7:deploy (default-deploy) on project reductio: Failed to deploy artifacts: Could not authenticate
```

**Solution:**
- Verify GitHub token has `write:packages` scope
- Check `~/.m2/settings.xml` configuration
- Ensure server `id` matches repository `id` in `pom.xml`

#### 2. Package Already Exists
```
[ERROR] Failed to deploy artifact: HTTP status: 409 - Conflict
```

**Solution:**
- GitHub Packages doesn't allow overwriting existing versions
- Increment version number and try again
- Or delete the existing package version in GitHub UI

#### 3. Workflow Permission Denied
```
Error: Resource not accessible by integration
```

**Solution:**
- Check repository settings → Actions → General
- Ensure "Read and write permissions" is enabled for GITHUB_TOKEN
- Or use a personal access token instead

#### 4. Invalid POM
```
[ERROR] Non-parseable POM: expected START_TAG or END_TAG not TEXT
```

**Solution:**
- Validate `pom.xml` syntax
- Check for unescaped characters in XML
- Ensure proper XML structure

### Debug Tips

1. **Enable Debug Output:**
   ```bash
   mvn deploy -DskipTests=true -X
   ```

2. **Validate POM:**
   ```bash
   mvn validate
   mvn help:effective-pom
   ```

3. **Test Local Build:**
   ```bash
   mvn clean install -DskipTests=true
   ```

4. **Check Package Status:**
   - Go to GitHub repository → Packages tab
   - Verify package visibility and versions

## Release Checklist

Before each release:

- [ ] All tests pass (`mvn test`)
- [ ] Code compiles without warnings (`mvn clean compile`)
- [ ] Version updated in `pom.xml`
- [ ] `RELEASE_NOTES.md` updated
- [ ] README.md version references updated
- [ ] Tag created and pushed
- [ ] GitHub Actions workflow completed successfully
- [ ] Package appears in GitHub Packages
- [ ] Installation instructions tested

## Support

For issues related to:
- **Library functionality:** Create GitHub issue
- **Publishing process:** Check this guide or GitHub Actions logs
- **GitHub Packages:** Consult [GitHub Packages documentation](https://docs.github.com/en/packages)

---

**Last Updated:** September 2025  
**Maintainer:** @sudojed