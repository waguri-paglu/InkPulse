package com.waguri.inkpulse.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.waguri.inkpulse.model.AppUser
import com.waguri.inkpulse.model.CommentItem
import com.waguri.inkpulse.model.ContentPost
import com.waguri.inkpulse.model.PostType
import com.waguri.inkpulse.model.UserRole

class OfflineDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "inkpulse_offline.db"
        const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val TABLE_POSTS = "posts"
        private const val TABLE_COMMENTS = "comments"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                display_name TEXT NOT NULL,
                email TEXT NOT NULL,
                password_hint TEXT NOT NULL,
                role TEXT NOT NULL,
                bio TEXT NOT NULL,
                badge TEXT NOT NULL,
                avatar_color INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_POSTS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                author_id INTEGER NOT NULL,
                author_name TEXT NOT NULL,
                author_handle TEXT NOT NULL,
                author_role TEXT NOT NULL,
                author_avatar_color INTEGER NOT NULL,
                post_type TEXT NOT NULL,
                title TEXT NOT NULL,
                subtitle TEXT NOT NULL,
                body TEXT NOT NULL,
                category TEXT NOT NULL,
                tags TEXT NOT NULL,
                reading_time INTEGER NOT NULL,
                is_draft INTEGER NOT NULL DEFAULT 0,
                is_featured INTEGER NOT NULL DEFAULT 0,
                likes_count INTEGER NOT NULL DEFAULT 0,
                is_liked INTEGER NOT NULL DEFAULT 0,
                is_bookmarked INTEGER NOT NULL DEFAULT 0,
                cover_color INTEGER NOT NULL,
                created_at INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_COMMENTS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                post_id INTEGER NOT NULL,
                author_name TEXT NOT NULL,
                author_handle TEXT NOT NULL,
                author_role TEXT NOT NULL,
                content TEXT NOT NULL,
                created_at INTEGER NOT NULL
            )
            """.trimIndent()
        )

        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COMMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_POSTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun resetDatabase() {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COMMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_POSTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        val now = System.currentTimeMillis()

        // 1. Seed Demo Accounts (Creators & Community Users)
        val alexId = insertUserRaw(
            db,
            username = "waguri_creator",
            displayName = "Waguri (Lead Creator)",
            email = "yashdaive@hotmail.com",
            passwordHint = "demo123",
            role = UserRole.CREATOR,
            bio = "Founder & Lead Editorial Creator on InkPulse. Building native Android tools directly from Termux on ARM64.",
            badge = "Lead Creator",
            avatarColor = 0xFF6366F1
        )

        val elenaId = insertUserRaw(
            db,
            username = "elena_editorial",
            displayName = "Elena Vance",
            email = "elena@inkpulse.studio",
            passwordHint = "creator123",
            role = UserRole.CREATOR,
            bio = "Design systems writer & mobile architecture columnist.",
            badge = "Featured Columnist",
            avatarColor = 0xFFEC4899
        )

        val samiraId = insertUserRaw(
            db,
            username = "samira_writes",
            displayName = "Samira Khan",
            email = "samira@community.dev",
            passwordHint = "user123",
            role = UserRole.USER,
            bio = "Community contributor sharing productivity notes, Linux tips, and indie app reviews.",
            badge = "Top Contributor",
            avatarColor = 0xFF10B981
        )

        val liamId = insertUserRaw(
            db,
            username = "liam_tinkerer",
            displayName = "Liam Chen",
            email = "liam@mobile.org",
            passwordHint = "user123",
            role = UserRole.USER,
            bio = "Android enthusiast writing tutorials, responses, and hardware notes.",
            badge = "Community Member",
            avatarColor = 0xFFF59E0B
        )

        // 2. Seed Creator Blogs & Community Content
        val post1Id = insertPostRaw(
            db,
            ContentPost(
                authorId = alexId,
                authorName = "Waguri (Lead Creator)",
                authorHandle = "@waguri_creator",
                authorRole = UserRole.CREATOR,
                authorAvatarColor = 0xFF6366F1,
                postType = PostType.CREATOR_BLOG,
                title = "Shipping Full Android Apps from Termux on an Android Phone",
                subtitle = "How proot-distro, GitHub CLI, and automated CI workflows turn a pocket phone into a complete engineering workstation.",
                body = """
                    ## The Pocket Workstation Era
                    
                    Most developers assume you need a heavy desktop rig to architect, write, and release native Android software. In reality, modern ARM64 phones paired with Termux and Ubuntu give you a full Linux environment in your pocket.
                    
                    ### How Our Workflow Works
                    - **Local Environment**: Ubuntu running inside Termux with Git and GitHub CLI (`gh`) authenticated.
                    - **Offline-First Architecture**: This app uses an embedded SQLite engine (`inkpulse_offline.db`) so creators can draft long-form blogs and community users can publish stories with zero network latency.
                    - **Automated Cloud Releases**: Every push triggers GitHub Actions to compile the Kotlin + Jetpack Compose source and publish a signed APK straight to GitHub Releases.
                    
                    > "The best development machine is the one you always have with you."
                    
                    Try switching between **Creator** and **Community Writer** accounts in the Account tab to test the full publishing pipeline!
                """.trimIndent(),
                category = "Engineering",
                tags = "Android, Termux, CI/CD, Offline-First",
                readingTimeMinutes = 5,
                isDraft = false,
                isFeatured = true,
                likesCount = 48,
                isLiked = true,
                isBookmarked = true,
                coverColor = 0xFF4F46E5,
                createdAt = now - 3600_000L * 18
            )
        )

        val post2Id = insertPostRaw(
            db,
            ContentPost(
                authorId = elenaId,
                authorName = "Elena Vance",
                authorHandle = "@elena_editorial",
                authorRole = UserRole.CREATOR,
                authorAvatarColor = 0xFFEC4899,
                postType = PostType.CREATOR_BLOG,
                title = "Designing Distraction-Free Writing Tools for Mobile Creators",
                subtitle = "Why clear typography, instant local persistence, and role-based workflows matter for modern publishing.",
                body = """
                    ## Writing on Glass Screens
                    
                    When creators write on phones or tablets, friction is the enemy. If an app waits on a remote API spinner every time you save a paragraph, your train of thought breaks.
                    
                    ### Three Pillars of InkPulse
                    1. **Instant Offline SQLite Persistence**: Every blog post, community story, draft, bookmark, and comment lives right on the device.
                    2. **Creator vs. Community Modes**: Creators get featured placement, editorial badges, and draft studio management, while Community Writers can jump right into sharing stories, tips, and replies.
                    3. **Markdown-Friendly Formatting**: Quick toolbar helpers let you structure headings, quotes, code snippets, and checklists effortlessly.
                """.trimIndent(),
                category = "Design",
                tags = "UI/UX, Writing, Material3, Mobile",
                readingTimeMinutes = 4,
                isDraft = false,
                isFeatured = true,
                likesCount = 31,
                isLiked = false,
                isBookmarked = false,
                coverColor = 0xFFDB2777,
                createdAt = now - 3600_000L * 12
            )
        )

        val post3Id = insertPostRaw(
            db,
            ContentPost(
                authorId = samiraId,
                authorName = "Samira Khan",
                authorHandle = "@samira_writes",
                authorRole = UserRole.USER,
                authorAvatarColor = 0xFF10B981,
                postType = PostType.USER_CONTENT,
                title = "My Favorite CLI Tools for Daily Terminal Productivity",
                subtitle = "A community roundup of ripgrep, fzf, tmux, and 7zip commands that save hours every week.",
                body = """
                    ## Community Tip: Supercharge Your Shell
                    
                    As a community writer here on InkPulse, I wanted to share the exact CLI toolkit I install on every fresh Ubuntu setup:
                    
                    - **tmux**: Keeps terminal sessions alive even when your phone switches apps.
                    - **ripgrep (`rg`)**: Searches thousands of code files in milliseconds.
                    - **gh (GitHub CLI)**: Lets you create repos, manage releases, and authenticate git without leaving the terminal.
                    - **7zip & zip**: Essential for inspecting build artifacts and release bundles.
                    
                    What packages are in your must-have list? Drop a comment below!
                """.trimIndent(),
                category = "Community Tips",
                tags = "Linux, Ubuntu, CLI, Productivity",
                readingTimeMinutes = 3,
                isDraft = false,
                isFeatured = false,
                likesCount = 24,
                isLiked = true,
                isBookmarked = false,
                coverColor = 0xFF059669,
                createdAt = now - 3600_000L * 6
            )
        )

        insertPostRaw(
            db,
            ContentPost(
                authorId = liamId,
                authorName = "Liam Chen",
                authorHandle = "@liam_tinkerer",
                authorRole = UserRole.USER,
                authorAvatarColor = 0xFFF59E0B,
                postType = PostType.USER_CONTENT,
                title = "Why Offline-First Apps Feel 10x Faster on Mobile",
                subtitle = "Testing local SQLite storage before wiring up cloud sync is the smartest way to prototype.",
                body = """
                    ## Local-First Is the Future
                    
                    Testing an app's UX shouldn't require spinning up Docker containers or paying for cloud databases on day one.
                    
                    With a local SQLite database and instant demo role switching, you can validate the entire Creator and Reader experience right on your phone, collect feedback, and plug in remote sync later when the interface is rock solid.
                """.trimIndent(),
                category = "Discussion",
                tags = "SQLite, Architecture, Prototyping",
                readingTimeMinutes = 2,
                isDraft = false,
                isFeatured = false,
                likesCount = 15,
                isLiked = false,
                isBookmarked = true,
                coverColor = 0xFFD97706,
                createdAt = now - 3600_000L * 3
            )
        )

        // Seed 1 Creator Draft so the Creator Studio shows drafts functionality immediately
        insertPostRaw(
            db,
            ContentPost(
                authorId = alexId,
                authorName = "Waguri (Lead Creator)",
                authorHandle = "@waguri_creator",
                authorRole = UserRole.CREATOR,
                authorAvatarColor = 0xFF6366F1,
                postType = PostType.CREATOR_BLOG,
                title = "[Draft] Roadmap for InkPulse v1.1: Cloud Sync & Markdown Export",
                subtitle = "Upcoming features planned after testing our v1.0 offline interface.",
                body = """
                    ## Upcoming in v1.1
                    
                    - Export blogs and community stories as `.md` files directly to Android storage.
                    - Optional Supabase / GitHub Gist sync while preserving 100% offline SQLite speed.
                    - Custom theme accents per creator publication.
                    
                    *(Tap 'Publish Now' in Studio -> Drafts to make this blog live!)*
                """.trimIndent(),
                category = "Announcements",
                tags = "Roadmap, v1.1, Draft",
                readingTimeMinutes = 2,
                isDraft = true,
                isFeatured = false,
                likesCount = 0,
                isLiked = false,
                isBookmarked = false,
                coverColor = 0xFF7C3AED,
                createdAt = now - 1800_000L
            )
        )

        // 3. Seed Comments
        insertCommentRaw(
            db,
            CommentItem(
                postId = post1Id,
                authorName = "Samira Khan",
                authorHandle = "@samira_writes",
                authorRole = UserRole.USER,
                content = "Running this whole workflow from Termux on Android is seriously next level!",
                createdAt = now - 3600_000L * 10
            )
        )
        insertCommentRaw(
            db,
            CommentItem(
                postId = post1Id,
                authorName = "Elena Vance",
                authorHandle = "@elena_editorial",
                authorRole = UserRole.CREATOR,
                content = "Love how snappy the offline SQLite feed feels. Zero loading spinners.",
                createdAt = now - 3600_000L * 5
            )
        )
        insertCommentRaw(
            db,
            CommentItem(
                postId = post3Id,
                authorName = "Waguri (Lead Creator)",
                authorHandle = "@waguri_creator",
                authorRole = UserRole.CREATOR,
                content = "Great list Samira! Just installed zip, 7zip, and gh earlier today.",
                createdAt = now - 3600_000L * 2
            )
        )
    }

    private fun insertUserRaw(
        db: SQLiteDatabase,
        username: String,
        displayName: String,
        email: String,
        passwordHint: String,
        role: UserRole,
        bio: String,
        badge: String,
        avatarColor: Long
    ): Long {
        val values = ContentValues().apply {
            put("username", username)
            put("display_name", displayName)
            put("email", email)
            put("password_hint", passwordHint)
            put("role", role.name)
            put("bio", bio)
            put("badge", badge)
            put("avatar_color", avatarColor)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    private fun insertPostRaw(db: SQLiteDatabase, post: ContentPost): Long {
        val values = ContentValues().apply {
            put("author_id", post.authorId)
            put("author_name", post.authorName)
            put("author_handle", post.authorHandle)
            put("author_role", post.authorRole.name)
            put("author_avatar_color", post.authorAvatarColor)
            put("post_type", post.postType.name)
            put("title", post.title)
            put("subtitle", post.subtitle)
            put("body", post.body)
            put("category", post.category)
            put("tags", post.tags)
            put("reading_time", post.readingTimeMinutes)
            put("is_draft", if (post.isDraft) 1 else 0)
            put("is_featured", if (post.isFeatured) 1 else 0)
            put("likes_count", post.likesCount)
            put("is_liked", if (post.isLiked) 1 else 0)
            put("is_bookmarked", if (post.isBookmarked) 1 else 0)
            put("cover_color", post.coverColor)
            put("created_at", post.createdAt)
        }
        return db.insert(TABLE_POSTS, null, values)
    }

    private fun insertCommentRaw(db: SQLiteDatabase, comment: CommentItem): Long {
        val values = ContentValues().apply {
            put("post_id", comment.postId)
            put("author_name", comment.authorName)
            put("author_handle", comment.authorHandle)
            put("author_role", comment.authorRole.name)
            put("content", comment.content)
            put("created_at", comment.createdAt)
        }
        return db.insert(TABLE_COMMENTS, null, values)
    }

    fun getAllUsers(): List<AppUser> {
        val users = mutableListOf<AppUser>()
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, null, null, null, null, null, "id ASC")
        cursor.use {
            while (it.moveToNext()) {
                users.add(
                    AppUser(
                        id = it.getLong(it.getColumnIndexOrThrow("id")),
                        username = it.getString(it.getColumnIndexOrThrow("username")),
                        displayName = it.getString(it.getColumnIndexOrThrow("display_name")),
                        email = it.getString(it.getColumnIndexOrThrow("email")),
                        passwordHint = it.getString(it.getColumnIndexOrThrow("password_hint")),
                        role = UserRole.valueOf(it.getString(it.getColumnIndexOrThrow("role"))),
                        bio = it.getString(it.getColumnIndexOrThrow("bio")),
                        badge = it.getString(it.getColumnIndexOrThrow("badge")),
                        avatarColor = it.getLong(it.getColumnIndexOrThrow("avatar_color"))
                    )
                )
            }
        }
        return users
    }

    fun createCustomDemoUser(
        displayName: String,
        username: String,
        email: String,
        role: UserRole,
        bio: String
    ): AppUser {
        val cleanHandle = username.trim().lowercase().replace(" ", "_").removePrefix("@")
            .ifBlank { "user_${System.currentTimeMillis() % 1000}" }
        val badge = if (role == UserRole.CREATOR) "Indie Creator" else "Community Writer"
        val color = if (role == UserRole.CREATOR) 0xFF6366F1L else 0xFF10B981L
        val db = writableDatabase
        val id = insertUserRaw(
            db = db,
            username = cleanHandle,
            displayName = displayName.ifBlank { cleanHandle },
            email = email.ifBlank { "$cleanHandle@inkpulse.local" },
            passwordHint = "demo",
            role = role,
            bio = bio.ifBlank { "Writing offline on InkPulse Android." },
            badge = badge,
            avatarColor = color
        )
        return getAllUsers().firstOrNull { it.id == id } ?: getAllUsers().first()
    }

    fun getAllPosts(): List<ContentPost> {
        val list = mutableListOf<ContentPost>()
        val db = readableDatabase
        val cursor = db.query(TABLE_POSTS, null, null, null, null, null, "created_at DESC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    ContentPost(
                        id = it.getLong(it.getColumnIndexOrThrow("id")),
                        authorId = it.getLong(it.getColumnIndexOrThrow("author_id")),
                        authorName = it.getString(it.getColumnIndexOrThrow("author_name")),
                        authorHandle = it.getString(it.getColumnIndexOrThrow("author_handle")),
                        authorRole = UserRole.valueOf(it.getString(it.getColumnIndexOrThrow("author_role"))),
                        authorAvatarColor = it.getLong(it.getColumnIndexOrThrow("author_avatar_color")),
                        postType = PostType.valueOf(it.getString(it.getColumnIndexOrThrow("post_type"))),
                        title = it.getString(it.getColumnIndexOrThrow("title")),
                        subtitle = it.getString(it.getColumnIndexOrThrow("subtitle")),
                        body = it.getString(it.getColumnIndexOrThrow("body")),
                        category = it.getString(it.getColumnIndexOrThrow("category")),
                        tags = it.getString(it.getColumnIndexOrThrow("tags")),
                        readingTimeMinutes = it.getInt(it.getColumnIndexOrThrow("reading_time")),
                        isDraft = it.getInt(it.getColumnIndexOrThrow("is_draft")) == 1,
                        isFeatured = it.getInt(it.getColumnIndexOrThrow("is_featured")) == 1,
                        likesCount = it.getInt(it.getColumnIndexOrThrow("likes_count")),
                        isLiked = it.getInt(it.getColumnIndexOrThrow("is_liked")) == 1,
                        isBookmarked = it.getInt(it.getColumnIndexOrThrow("is_bookmarked")) == 1,
                        coverColor = it.getLong(it.getColumnIndexOrThrow("cover_color")),
                        createdAt = it.getLong(it.getColumnIndexOrThrow("created_at"))
                    )
                )
            }
        }
        return list
    }

    fun savePost(post: ContentPost): Long {
        val db = writableDatabase
        return if (post.id == 0L) {
            insertPostRaw(db, post)
        } else {
            val values = ContentValues().apply {
                put("title", post.title)
                put("subtitle", post.subtitle)
                put("body", post.body)
                put("category", post.category)
                put("tags", post.tags)
                put("reading_time", post.readingTimeMinutes)
                put("is_draft", if (post.isDraft) 1 else 0)
                put("is_featured", if (post.isFeatured) 1 else 0)
                put("cover_color", post.coverColor)
            }
            db.update(TABLE_POSTS, values, "id = ?", arrayOf(post.id.toString()))
            post.id
        }
    }

    fun deletePost(postId: Long) {
        val db = writableDatabase
        db.delete(TABLE_COMMENTS, "post_id = ?", arrayOf(postId.toString()))
        db.delete(TABLE_POSTS, "id = ?", arrayOf(postId.toString()))
    }

    fun toggleLike(post: ContentPost) {
        val db = writableDatabase
        val newLiked = !post.isLiked
        val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
        val values = ContentValues().apply {
            put("is_liked", if (newLiked) 1 else 0)
            put("likes_count", newCount)
        }
        db.update(TABLE_POSTS, values, "id = ?", arrayOf(post.id.toString()))
    }

    fun toggleBookmark(post: ContentPost) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("is_bookmarked", if (!post.isBookmarked) 1 else 0)
        }
        db.update(TABLE_POSTS, values, "id = ?", arrayOf(post.id.toString()))
    }

    fun toggleFeatured(post: ContentPost) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("is_featured", if (!post.isFeatured) 1 else 0)
        }
        db.update(TABLE_POSTS, values, "id = ?", arrayOf(post.id.toString()))
    }

    fun publishDraft(postId: Long) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("is_draft", 0)
            put("created_at", System.currentTimeMillis())
        }
        db.update(TABLE_POSTS, values, "id = ?", arrayOf(postId.toString()))
    }

    fun getCommentsForPost(postId: Long): List<CommentItem> {
        val comments = mutableListOf<CommentItem>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_COMMENTS,
            null,
            "post_id = ?",
            arrayOf(postId.toString()),
            null,
            null,
            "created_at ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                comments.add(
                    CommentItem(
                        id = it.getLong(it.getColumnIndexOrThrow("id")),
                        postId = it.getLong(it.getColumnIndexOrThrow("post_id")),
                        authorName = it.getString(it.getColumnIndexOrThrow("author_name")),
                        authorHandle = it.getString(it.getColumnIndexOrThrow("author_handle")),
                        authorRole = UserRole.valueOf(it.getString(it.getColumnIndexOrThrow("author_role"))),
                        content = it.getString(it.getColumnIndexOrThrow("content")),
                        createdAt = it.getLong(it.getColumnIndexOrThrow("created_at"))
                    )
                )
            }
        }
        return comments
    }

    fun addComment(postId: Long, user: AppUser, content: String) {
        if (content.isBlank()) return
        val db = writableDatabase
        insertCommentRaw(
            db,
            CommentItem(
                postId = postId,
                authorName = user.displayName,
                authorHandle = "@${user.username}",
                authorRole = user.role,
                content = content.trim()
            )
        )
    }

    fun getTotalCommentCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_COMMENTS", null)
        cursor.use {
            return if (it.moveToFirst()) it.getInt(0) else 0
        }
    }
}
