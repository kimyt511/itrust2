# 🚀 Contributing to iTrust2 Project

Welcome to the iTrust2 development guide! Here are the rules and guidelines to ensure smooth collaboration and consistency throughout the project.

## 1. Starting Point

Before diving into changes, please either discuss them with the team or post them in the issues or discussion section following the provided templates. Whether it's a new feature or a potential bug fix, getting feedback or documenting it ensures we avoid duplicate efforts and potential conflicts.

## 2. Clone & Create a Branch

- **Clone the repo** into your local machine:

  - HTTPS:

    ```bash
    git clone https://34.64.100.199/team4se/itrust2.git
    ```

  - SSH:

    ```bash
    git clone git@34.64.100.199:team4se/itrust2.git
    ```

- **Navigate to your local repo**: `cd itrust2`.

- **Create a branch** for your feature or bugfix:

  ```bash
  git checkout -b <branch-name>
  ```

## 3. Setup Your Environment

Before you begin your work, ensure your environment is correctly configured:

- **Maven Setup**:

  We use Maven to manage dependencies and build the project. Install it from [here](https://maven.apache.org/download.cgi).

  Then, build the project with:

  ```bash
  mvn clean install
  ```

## 4. Committing Your Changes

Before committing, ensure you've set up `pre-commit` hooks as mentioned earlier in the setup section. This ensures that your code adheres to the project's coding standards.

Follow the commit message convention using gitmoji:

```
<emoji><type>: <description>
```

Where `<emoji>` and `<type>` can be one of the following:

- ✨`feat`: For introducing new features
- 🐛`fix`: For bug fixes
- 🎨`style`: For changes related to styling and appearance
- 📝`docs`: Documentation-only changes
- ♻️`refactor`: Code refactoring without changing any logic
- ⚡️`perf`: Code changes to improve performance
- ✅`test`: Adding missing tests or corrections
- 🔧`chore`: Build process or auxiliary tool changes
- 🔀`merge`: Merging branches or introducing changes from upstream
- ⏪`revert`: Reverting changes

Example:

```
✨feat: Add new API endpoint for prices
```

Make sure your commit messages clearly describe what the commit does. In addition to the above, you can use other gitmojis that fit the nature of the change. Consult the [gitmoji guide](https://gitmoji.dev) for more emojis and their meanings.

## 5. Submitting a Pull Request

- Push your branch to the repo on Gitlab:

  ```bash
  git push origin <branch-name>
  ```

- **Open a pull request** to the `develop` branch. Make sure to follow the provided template.

- Once the pull request is approved and merged, you can pull the changes from the `develop` branch and delete your branch.

---

## 📖 Further Reading

- [Poetry Documentation](https://python-poetry.org/docs/)
- [Pre-commit Documentation](https://pre-commit.com/)
- [Git and GitHub Learning Resources](https://docs.github.com/en/get-started/quickstart/git-and-github-learning-resources)

---

Your collaboration is what makes this project thrive! Let's keep up the great work together. 🚀

---
