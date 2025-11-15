# CLAUDE.md - AI Assistant Guide for license-model

This document provides comprehensive guidance for AI assistants working on the license-model repository.

## Repository Overview

**Repository:** license-model
**Purpose:** This repository is designed for software license modeling, management, and validation systems.
**Status:** Early-stage project (initialized)

### Project Goals
- Model and represent various software license types
- Provide license validation and compliance checking
- Enable license management workflows
- Support license compatibility analysis

## Repository Structure

Currently, this is a new repository. The recommended structure for future development is:

```
license-model/
├── CLAUDE.md              # This file - AI assistant guide
├── README.md              # Project documentation for users
├── LICENSE                # Repository license
├── .gitignore            # Git ignore patterns
├── src/                  # Source code
│   ├── models/           # License data models
│   ├── validators/       # License validation logic
│   ├── parsers/          # License file parsers
│   ├── utils/            # Utility functions
│   └── index.js/ts       # Main entry point
├── tests/                # Test files
│   ├── unit/             # Unit tests
│   ├── integration/      # Integration tests
│   └── fixtures/         # Test data and fixtures
├── docs/                 # Additional documentation
│   ├── api.md            # API documentation
│   ├── licenses/         # License type documentation
│   └── examples.md       # Usage examples
├── examples/             # Example code and usage
├── scripts/              # Build and utility scripts
└── package.json          # Node.js dependencies (if applicable)
```

## Development Workflows

### 1. Adding New Features

When implementing new features:

1. **Understand Requirements**
   - Read the issue/request carefully
   - Clarify ambiguities before coding
   - Identify affected components

2. **Plan Implementation**
   - Use TodoWrite to break down tasks
   - Consider impacts on existing code
   - Design before coding

3. **Write Code**
   - Follow coding conventions (see below)
   - Write tests alongside features
   - Document as you go

4. **Test Thoroughly**
   - Run all tests before committing
   - Add new tests for new functionality
   - Verify edge cases

5. **Commit and Push**
   - Write clear, descriptive commit messages
   - Follow git branch requirements
   - Push to the designated feature branch

### 2. Bug Fixes

For bug fixes:

1. Reproduce the bug
2. Write a failing test that demonstrates it
3. Fix the issue
4. Verify the test passes
5. Ensure no regressions
6. Document the fix in commit messages

### 3. Code Review Preparation

Before requesting review:

- Ensure code follows conventions
- All tests pass
- Documentation is updated
- Commit history is clean
- No debugging code remains

## Coding Conventions

### General Principles

1. **Clarity over Cleverness**
   - Write readable, maintainable code
   - Prefer explicit over implicit
   - Use descriptive names

2. **Consistency**
   - Follow existing patterns in the codebase
   - Use consistent formatting
   - Maintain consistent naming conventions

3. **Documentation**
   - Document complex logic
   - Add JSDoc/TSDoc comments for public APIs
   - Keep comments up-to-date

### Naming Conventions

- **Files:** Use kebab-case for file names (e.g., `license-validator.js`)
- **Classes:** Use PascalCase (e.g., `LicenseModel`)
- **Functions/Variables:** Use camelCase (e.g., `validateLicense`)
- **Constants:** Use UPPER_SNAKE_CASE (e.g., `MAX_LICENSE_LENGTH`)
- **Private members:** Prefix with underscore (e.g., `_internalMethod`)

### Code Style

Once the primary language is determined, follow these guidelines:

**JavaScript/TypeScript:**
- Use 2-space indentation
- Use semicolons
- Prefer `const` over `let`, avoid `var`
- Use async/await over raw promises
- Use template literals over string concatenation
- Prefer destructuring where appropriate

**Python:**
- Follow PEP 8
- Use 4-space indentation
- Use type hints for function signatures
- Use docstrings for modules, classes, and functions

**General:**
- Keep functions focused and small
- Limit line length to 80-100 characters
- Use meaningful variable names
- Avoid deep nesting (max 3-4 levels)

### Testing Conventions

- Test files should mirror source structure
- Name test files with `.test.js` or `.spec.js` suffix
- Use descriptive test names that explain the scenario
- Follow AAA pattern: Arrange, Act, Assert
- Mock external dependencies
- Aim for high test coverage, especially for critical paths

### Error Handling

- Use try-catch blocks appropriately
- Throw meaningful error messages
- Create custom error classes for domain-specific errors
- Log errors with sufficient context
- Never swallow errors silently

## License Types to Support

Common license types this system should handle:

### Permissive Licenses
- MIT
- Apache 2.0
- BSD (2-Clause, 3-Clause)
- ISC

### Copyleft Licenses
- GPL (v2, v3)
- LGPL
- AGPL
- MPL 2.0

### Other Licenses
- Creative Commons variants
- Proprietary licenses
- Custom licenses

## AI Assistant Guidelines

### When Writing Code

1. **Security First**
   - Never introduce vulnerabilities (XSS, SQL injection, command injection)
   - Validate all inputs
   - Sanitize outputs
   - Use parameterized queries
   - Follow OWASP best practices

2. **Performance Considerations**
   - Consider time/space complexity
   - Avoid unnecessary loops
   - Cache when appropriate
   - Be mindful of memory usage

3. **Maintainability**
   - Write self-documenting code
   - Add comments for complex logic
   - Keep functions focused
   - Avoid code duplication (DRY principle)

### When Analyzing Code

1. Use Task tool with Explore agent for codebase exploration
2. Read existing files before making changes
3. Understand the full context before proposing solutions
4. Check for similar existing implementations

### When Making Changes

1. **Always:**
   - Read files before editing
   - Run tests after changes
   - Update documentation
   - Follow existing patterns
   - Use TodoWrite for multi-step tasks

2. **Never:**
   - Make assumptions about file contents
   - Skip testing
   - Commit commented-out code
   - Leave TODO comments without tracking
   - Hardcode sensitive information

### Git Workflow

1. **Branch Strategy**
   - Work on designated feature branches
   - Branch names start with `claude/`
   - Never push to main/master without permission

2. **Commit Messages**
   - Use clear, descriptive messages
   - Start with a verb (Add, Fix, Update, Remove, Refactor)
   - Reference issues when applicable
   - Keep first line under 72 characters

3. **Before Committing**
   - Run all tests
   - Check for linting errors
   - Review your changes
   - Remove debug code

### Common Tasks

#### Adding a New License Type

1. Create model in `src/models/`
2. Add parser in `src/parsers/`
3. Add validator in `src/validators/`
4. Write tests in `tests/unit/`
5. Update documentation in `docs/licenses/`
6. Add examples in `examples/`

#### Adding a New Validation Rule

1. Implement rule in `src/validators/`
2. Write comprehensive tests
3. Document the rule
4. Update API documentation
5. Add usage examples

#### Fixing a Bug

1. Write a test that reproduces the bug
2. Fix the issue
3. Verify the test passes
4. Check for similar issues
5. Update documentation if needed

## Dependencies and Tools

### Recommended Tools (to be added as needed)

- **Testing:** Jest, Mocha, or pytest
- **Linting:** ESLint (JS/TS) or flake8 (Python)
- **Formatting:** Prettier (JS/TS) or Black (Python)
- **Type Checking:** TypeScript or mypy
- **Documentation:** JSDoc, TypeDoc, or Sphinx
- **CI/CD:** GitHub Actions

### Security Scanning

- Run dependency vulnerability scans regularly
- Keep dependencies up-to-date
- Review security advisories
- Use tools like npm audit, Snyk, or Dependabot

## Documentation Standards

### Code Comments

- Explain **why**, not **what** (code shows what)
- Document complex algorithms
- Note non-obvious behaviors
- Include examples for public APIs

### Function Documentation

```javascript
/**
 * Validates a license against specified rules
 * @param {string} licenseText - The license text to validate
 * @param {Object} options - Validation options
 * @param {boolean} options.strict - Enable strict validation
 * @returns {ValidationResult} Result with errors and warnings
 * @throws {InvalidLicenseError} If license format is invalid
 */
function validateLicense(licenseText, options = {}) {
  // Implementation
}
```

### README Updates

Keep README.md updated with:
- Project description
- Installation instructions
- Usage examples
- API overview
- Contributing guidelines
- License information

## Testing Strategy

### Test Coverage Goals

- Aim for 80%+ code coverage
- 100% coverage for critical paths
- Test edge cases and error conditions
- Include integration tests

### Test Organization

```
tests/
├── unit/                 # Fast, isolated tests
│   ├── models/
│   ├── validators/
│   └── parsers/
├── integration/          # Tests with dependencies
│   ├── api/
│   └── workflows/
└── fixtures/            # Test data
    ├── licenses/
    └── configs/
```

### Test Naming

- Use descriptive names: `test_validates_MIT_license_successfully`
- Group related tests in describe/context blocks
- Use beforeEach/afterEach for setup/teardown

## Performance Considerations

1. **License Parsing**
   - Cache parsed licenses
   - Use streaming for large files
   - Implement timeout limits

2. **Validation**
   - Short-circuit on first failure (when appropriate)
   - Parallelize independent validations
   - Memoize expensive computations

3. **Database Access** (if applicable)
   - Use connection pooling
   - Index frequently queried fields
   - Implement pagination for large result sets

## Security Best Practices

1. **Input Validation**
   - Validate all external inputs
   - Whitelist acceptable values
   - Reject malformed data early

2. **Output Encoding**
   - Escape output based on context
   - Use safe APIs for rendering

3. **Dependency Management**
   - Regular security audits
   - Pin dependency versions
   - Review dependency licenses

4. **Error Messages**
   - Don't leak sensitive information
   - Log detailed errors internally
   - Return generic errors to users

## Troubleshooting

### Common Issues

1. **Test Failures**
   - Check test isolation
   - Verify fixture data
   - Review recent changes

2. **Performance Issues**
   - Profile the code
   - Check for N+1 queries
   - Review algorithm complexity

3. **Build Failures**
   - Verify dependencies are installed
   - Check Node/Python version
   - Review build logs

## Resources

### License Information
- [SPDX License List](https://spdx.org/licenses/)
- [Choose a License](https://choosealicense.com/)
- [OSI Approved Licenses](https://opensource.org/licenses)
- [GNU Licenses](https://www.gnu.org/licenses/)

### Best Practices
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Semantic Versioning](https://semver.org/)
- [Keep a Changelog](https://keepachangelog.com/)
- [Conventional Commits](https://www.conventionalcommits.org/)

## Version History

- **2025-11-15:** Initial CLAUDE.md created for new repository
- Repository initialized with basic structure
- AI assistant guidelines established

## Contact and Support

For questions or issues:
1. Check existing documentation
2. Review similar code in the repository
3. Consult the resources listed above
4. Ask the repository maintainers

---

**Last Updated:** 2025-11-15
**Repository Status:** Initialized - Ready for development
