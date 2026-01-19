# Fix: Personal Access Token Workflow Scope

## Problem
Your Personal Access Token doesn't have the `workflow` scope, which is required to push GitHub Actions workflow files.

## Solution: Create New Token with Workflow Scope

### Step-by-Step Instructions:

1. **Go to GitHub Token Settings**
   - Visit: https://github.com/settings/tokens
   - Or: GitHub → Your Profile → Settings → Developer settings → Personal access tokens → Tokens (classic)

2. **Generate New Token**
   - Click **"Generate new token"** → **"Generate new token (classic)"**

3. **Configure Token**
   - **Note**: `PlayWrigthAutomation-Workflow` (or any name you prefer)
   - **Expiration**: Choose your preferred duration (90 days, 1 year, or no expiration)
   
4. **Select Required Scopes**
   - ✅ **repo** (Full control of private repositories)
     - This includes: repo:status, repo_deployment, public_repo, repo:invite, security_events
   - ✅ **workflow** (Update GitHub Action workflows) - **REQUIRED**
   
5. **Generate and Copy**
   - Click **"Generate token"** at the bottom
   - **IMPORTANT**: Copy the token immediately - you won't see it again!

6. **Update Git Credentials**
   
   **Option A: Use the token when prompted**
   ```bash
   git push --set-upstream origin playwright_automation_local_branch
   ```
   - Username: `Priyada1`
   - Password: Paste your NEW token (not your GitHub password)
   
   **Option B: Store in macOS Keychain**
   ```bash
   # This will store credentials securely
   git config --global credential.helper osxkeychain
   
   # Then push (will prompt once, then remember)
   git push --set-upstream origin playwright_automation_local_branch
   ```

## Alternative: Push Without Workflow Files First

If you want to push other changes first:

```bash
# 1. Temporarily remove workflow files from staging
git reset HEAD .github/workflows/

# 2. Commit other changes
git commit -m "Add CI/CD documentation and test updates"

# 3. Push without workflow files
git push --set-upstream origin playwright_automation_local_branch

# 4. Later, after updating token with workflow scope:
git add .github/workflows/
git commit -m "Add CI/CD workflows"
git push origin playwright_automation_local_branch
```

## Verify Token Has Workflow Scope

After creating the token, you can verify it has the workflow scope by checking:
- The token list shows "workflow" in the scopes column
- Or try pushing again - it should work!

## Security Note

- Never commit tokens to git
- Use environment variables or GitHub Secrets for CI/CD
- Rotate tokens regularly
- Revoke old tokens after creating new ones

