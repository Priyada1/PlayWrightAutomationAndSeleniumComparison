# CI/CD Setup Guide

This project uses **GitHub Actions** for Continuous Integration and Continuous Deployment.

## 📋 Workflows

### 1. **CI/CD Pipeline** (`.github/workflows/ci-cd.yml`)

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop` branches
- Manual trigger via GitHub Actions UI

**What it does:**
1. ✅ Checks out code
2. ✅ Sets up Java 17
3. ✅ Caches Maven dependencies
4. ✅ Installs Playwright browsers
5. ✅ Runs tests in headless mode
6. ✅ Generates Allure reports
7. ✅ Uploads test results and reports as artifacts
8. ✅ Publishes test results as comments on PRs
9. ✅ Deploys Allure report to GitHub Pages (main branch only)

### 2. **Nightly Tests** (`.github/workflows/nightly-tests.yml`)

**Triggers:**
- Daily at 2:00 AM UTC
- Manual trigger via GitHub Actions UI

**What it does:**
- Runs full test suite nightly
- Generates and stores reports for 90 days
- Useful for regression testing

## 🚀 Setup Instructions

### Step 1: Enable GitHub Pages (for Allure Reports)

1. Go to your repository on GitHub
2. Click **Settings** → **Pages**
3. Under **Source**, select:
   - **Source**: `gh-pages` branch
   - **Folder**: `/ (root)`
4. Click **Save**

### Step 2: Push the Workflow Files

```bash
git add .github/workflows/
git add src/test/java/PlayWrightTest.java
git commit -m "Add CI/CD workflows with GitHub Actions"
git push origin main
```

### Step 3: Verify CI/CD is Working

1. Go to your repository on GitHub
2. Click the **Actions** tab
3. You should see the workflow running
4. Click on a workflow run to see details

## 📊 Viewing Results

### Test Results
- **GitHub Actions**: Go to **Actions** tab → Click on a workflow run
- **Artifacts**: Download from the workflow run page
- **PR Comments**: Test results are automatically posted on Pull Requests

### Allure Reports
- **GitHub Pages**: `https://<username>.github.io/<repository-name>/allure-report/`
- **Artifacts**: Download from workflow run page

## 🔧 Configuration

### Environment Variables

The CI/CD pipeline uses these environment variables:

- `HEADLESS=true` - Runs browser in headless mode (required for CI)

### Customization

You can customize the workflows by editing:
- `.github/workflows/ci-cd.yml` - Main CI/CD pipeline
- `.github/workflows/nightly-tests.yml` - Nightly test schedule

### Common Customizations

#### Change Java Version
```yaml
java-version: [17, 21]  # Test on multiple Java versions
```

#### Change Test Schedule
```yaml
schedule:
  - cron: '0 2 * * *'  # Daily at 2 AM UTC
  - cron: '0 0 * * 0'  # Weekly on Sunday
```

#### Add More Browsers
```yaml
- name: Install Playwright browsers
  run: |
    npx playwright install --with-deps chromium firefox webkit
```

#### Run Tests in Parallel
```yaml
strategy:
  matrix:
    browser: [chromium, firefox, webkit]
```

## 📦 Artifacts

The CI/CD pipeline generates and stores:

1. **Allure Results** (`target/allure-results/`)
   - JSON test results
   - Retained for 30 days

2. **Allure Report** (`target/site/allure-maven-plugin/`)
   - HTML report
   - Retained for 30 days
   - Published to GitHub Pages (main branch)

3. **Test Results** (`target/surefire-reports/`)
   - XML test results
   - Retained for 30 days

## 🔍 Monitoring

### Workflow Status Badge

Add this to your README.md:

```markdown
![CI/CD](https://github.com/<username>/<repository>/workflows/CI/CD%20Pipeline/badge.svg)
```

### Notifications

GitHub will notify you:
- ✅ When workflows succeed
- ❌ When workflows fail
- 📧 Via email (if configured in GitHub settings)

## 🐛 Troubleshooting

### Tests Fail in CI but Pass Locally

1. **Check headless mode**: CI runs in headless mode
2. **Check timeouts**: CI might be slower
3. **Check environment**: CI uses Ubuntu, not macOS

### Playwright Browsers Not Installing

The workflow uses `continue-on-error: true` for browser installation.
Playwright Java will download browsers automatically on first test run.

### GitHub Pages Not Updating

1. Check if `gh-pages` branch exists
2. Verify GitHub Pages is enabled in Settings
3. Check workflow logs for deployment errors

## 📚 Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Playwright CI/CD Guide](https://playwright.dev/java/docs/ci)
- [Allure Reporting](https://docs.qameta.io/allure/)

## 🎯 Next Steps

1. ✅ Push workflow files to GitHub
2. ✅ Enable GitHub Pages
3. ✅ Monitor first workflow run
4. ✅ Customize workflows as needed
5. ✅ Add status badges to README

