# Git Setup Instructions for PlayWrigthAutomation

## Step 1: Create Repository on GitHub

1. Go to: https://github.com/new
2. Repository name: `PlayWrigthAutomation` (or any name you prefer)
3. Description: "Playwright Automation Framework with Page Object Model"
4. **DO NOT** check:
   - ❌ Add a README file
   - ❌ Add .gitignore
   - ❌ Choose a license
5. Click **"Create repository"**

## Step 2: Add Remote and Push

After creating the repository, GitHub will show you the repository URL. Use one of these commands:

### Option A: Using HTTPS (Recommended)
```bash
# Add the remote (replace REPOSITORY_NAME with your actual repo name)
git remote add origin https://github.com/Priyada1/REPOSITORY_NAME.git

# Push to GitHub
git push -u origin main
```

### Option B: Using SSH (if you have SSH keys set up)
```bash
# Add the remote
git remote add origin git@github.com:Priyada1/REPOSITORY_NAME.git

# Push to GitHub
git push -u origin main
```

## Step 3: Verify

```bash
# Check remote is set correctly
git remote -v

# Should show:
# origin  https://github.com/Priyada1/REPOSITORY_NAME.git (fetch)
# origin  https://github.com/Priyada1/REPOSITORY_NAME.git (push)
```

## Common Repository Names

If you want to use a specific name, here are suggestions:
- `PlayWrigthAutomation`
- `playwright-automation-framework`
- `playwright-pom-framework`
- `java-playwright-tests`

## Troubleshooting

### If you get "repository not found":
- Make sure the repository name matches exactly
- Check that you're logged into GitHub
- Verify the repository exists at: https://github.com/Priyada1/REPOSITORY_NAME

### If you get authentication errors:
- For HTTPS: You may need a Personal Access Token
- For SSH: Make sure your SSH key is added to GitHub

### To update remote URL later:
```bash
# Remove existing remote
git remote remove origin

# Add new remote
git remote add origin https://github.com/Priyada1/REPOSITORY_NAME.git
```

