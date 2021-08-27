# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
- Clean code!
- Tests
- Support for Transitions into Turing Machines (as state)

## [2.0.2] - 2021-08-26
### Removed
- Removed the old bin directory as data is available as resources 
### Security
- Fixes for CWE-78, CWE-88

## [2.0.1] - 2021-08-26
### Fixed
- Authors sample machine to only halt on correct inputs

## [2.0.0] - 2021-08-26
### Added
- This CHANGELOG to help explain evolving code state 
- An AUTHORS.md to list authors
### Changed
- Builds using Gradle Wrapper
- Runs using Java 12 and uses a few features changed since 1.4, e.g. Generics, Enhanced For, Vars
- Upgraded JavaHelp version to the latest available in mavenCentral
- Taken warts and all but applied Google Java Format (AOSP) style formatting to improve readability
- README.md to use inclusive language after change to main branch
### Removed
- Some unused code paths

## [1.0.0] - 2004-06
### Added
- Original release (3rd Year B.Sc. Comp Sci project at King's College London) using Java 1.4
- Ability to simulate, design, and print Turing Machines
- A README.md explaining the purpose of the project

[2.0.2]: https://github.com/blacish/visuturing/compare/v2.0.1...v2.0.2
[2.0.1]: https://github.com/blacish/visuturing/compare/v2.0.0...v2.0.1
[2.0.0]: https://github.com/blacish/visuturing/compare/v1.0.0...v2.0.0
[1.0.0]: https://github.com/blacish/visuturing/releases/tag/v1.0.0