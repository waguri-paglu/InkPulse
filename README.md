# InkPulse — Creator Blog & Community Content Android App

**InkPulse** is an offline-first native Android application built with **Kotlin**, **Jetpack Compose (Material 3)**, and an embedded **SQLite** database (`inkpulse_offline.db`). It is designed so **Creators** can write and manage long-form blogs and **Community Users** can publish posts, stories, tips, and discussions—with instant demo login switching for rapid interface testing.

## Features

- **One-Tap Demo Logins & Role Switcher**:
  - **Creator Accounts** (`@waguri_creator`, `@elena_editorial`): Unlock the Creator Blog Studio, Featured Carousel pinning, Drafts Manager, and Studio Analytics.
  - **Community Writer Accounts** (`@samira_writes`, `@liam_tinkerer`): Publish community posts, tutorials, discussions, bookmark articles, and comment.
  - **Custom Demo Profiles**: Create new Creator or User accounts directly in the offline database.
- **Role-Aware Writing Composer**:
  - Quick Markdown formatting snippets (`## H2`, `### H3`, `- Bullet`, `> Quote`).
  - Cover accent theme picker, category chips, word count & reading time estimator.
  - Save as an **Offline Draft** or **Publish Immediately**.
- **100% Offline SQLite Persistence (`inkpulse_offline.db`)**:
  - Pre-seeded with sample Creator Blogs, Community Stories, an unpublished Creator Draft, and discussion comments.
  - Includes an in-app **Database Inspector** and a **Reset Demo DB** button to restore initial seed data anytime.
- **Automated CI/CD Release (`.github/workflows/build.yml`)**:
  - Compiles both Debug and signed Release APKs via GitHub Actions and publishes them directly to **GitHub Releases** (`v1.0.0`).
