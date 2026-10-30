# EasyBank Microservices – Docker Build Scripts

## Overview

This directory contains automation scripts for building Docker images for the EasyBank microservices.

The `build-all.sh` script automates the building of all four microservices using the Jib Maven Plugin.

It also provides timestamped logging, error handling, and build summaries.

## Project Structure

```text
EasyBank/
│
├── accounts/
│   └── pom.xml
│
├── cards/
│   └── pom.xml
│
├── loans/
│   └── pom.xml
│
├── configserver/
│   └── pom.xml
│
└── build-scripts/
    ├── build-all.sh
    ├── README.md
    └── logs/
```

## Prerequisites

Ensure that the following are installed and configured:

- Linux operating system.
- Java Development Kit.
- Maven or Maven Wrapper.
- Docker Engine.
- Jib Maven Plugin in each microservice's POM.

## Microservices

| Service | Docker Image |
|---|---|
| Accounts | `marcell13/accounts` |
| Cards | `marcell13/cards` |
| Loans | `marcell13/loans` |
| Config Server | `marcell13/configserver` |

## Installation

Navigate to the build-scripts directory:

```bash
cd build-scripts
```

Make the script executable:

```bash
chmod +x build-all.sh
```

## Usage

### Build all microservices

```bash
./build-all.sh
```

The default Docker image tag is `s6`.

### Specify a custom image tag

```bash
./build-all.sh s7
```

This builds all images using the `s7` tag.

The script automatically locates the EasyBank project root, so it does not matter which directory you execute it from.

For example:

```bash
/path/to/EasyBank/build-scripts/build-all.sh s8
```

## Logging

All build logs are stored inside:

```text
build-scripts/logs/
```

Each execution generates a separate timestamped log file.

Example:

```text
build_20261030_213000.log
```

Logs capture:

- Build start and completion times.
- Microservice being built.
- Docker image name and tag.
- Maven and Jib output.
- Success and failure messages.
- Final build summary.

### View available logs

```bash
ls -lh logs/
```

### Read a log

```bash
less logs/build_20261030_213000.log
```

### Monitor a running build

```bash
tail -f logs/build_20261030_213000.log
```

## Build Verification

After a successful build, verify the generated images:

```bash
docker images | grep marcell13
```

Expected images:

```text
marcell13/accounts       s7
marcell13/cards          s7
marcell13/loans          s7
marcell13/configserver   s7
```

## Error Handling

The script builds microservices sequentially.

If a build fails:

1. The error is captured in the log.
2. The script stops processing subsequent microservices.
3. A summary of successful and failed builds is displayed.
4. The script exits with status code `1`.

A successful execution returns status code `0`.

Check the exit status:

```bash
echo $?
```

## Jib Maven Configuration

Each microservice must configure Jib to accept the image name dynamically:

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.5.2</version>
    <configuration>
        <to>
            <image>${docker.image.name}</image>
        </to>
    </configuration>
</plugin>
```

## Future Enhancements

- Docker Hub image publishing.
- Automated image cleanup.
- Parallel builds.
- Docker Compose deployment.
- CI/CD pipeline integration.

---

**Project:** EasyBank Microservices  
**Build Tool:** Maven + Jib  
**Container Runtime:** Docker Engine
