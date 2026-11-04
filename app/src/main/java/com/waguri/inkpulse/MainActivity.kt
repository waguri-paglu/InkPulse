package com.waguri.inkpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.waguri.inkpulse.data.OfflineDatabaseHelper
import com.waguri.inkpulse.model.AppUser
import com.waguri.inkpulse.model.ContentPost
import com.waguri.inkpulse.ui.screens.LoginScreen
import com.waguri.inkpulse.ui.screens.MainAppScreen
import com.waguri.inkpulse.ui.screens.PostDetailScreen
import com.waguri.inkpulse.ui.theme.InkPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = OfflineDatabaseHelper(applicationContext)

        setContent {
            var darkTheme by remember { mutableStateOf(true) }
            var refreshTick by remember { mutableIntStateOf(0) }

            val users = remember(refreshTick) { dbHelper.getAllUsers() }
            val posts = remember(refreshTick) { dbHelper.getAllPosts() }
            val totalCommentsCount = remember(refreshTick) { dbHelper.getTotalCommentCount() }

            var currentUser by remember { mutableStateOf<AppUser?>(null) }
            var selectedPostId by remember { mutableStateOf<Long?>(null) }
            var editingPost by remember { mutableStateOf<ContentPost?>(null) }

            val activePost = posts.firstOrNull { it.id == selectedPostId }

            InkPulseTheme(darkTheme = darkTheme) {
                val loggedInUser = currentUser
                if (loggedInUser == null) {
                    LoginScreen(
                        demoUsers = users,
                        onSelectUser = { user ->
                            currentUser = user
                        },
                        onCreateCustomUser = { displayName, username, email, role, bio ->
                            val created = dbHelper.createCustomDemoUser(
                                displayName = displayName,
                                username = username,
                                email = email,
                                role = role,
                                bio = bio
                            )
                            refreshTick++
                            currentUser = created
                        }
                    )
                } else if (activePost != null) {
                    BackHandler {
                        selectedPostId = null
                    }
                    val comments = remember(activePost.id, refreshTick) {
                        dbHelper.getCommentsForPost(activePost.id)
                    }
                    PostDetailScreen(
                        post = activePost,
                        currentUser = loggedInUser,
                        comments = comments,
                        onBack = { selectedPostId = null },
                        onToggleLike = {
                            dbHelper.toggleLike(activePost)
                            refreshTick++
                        },
                        onToggleBookmark = {
                            dbHelper.toggleBookmark(activePost)
                            refreshTick++
                        },
                        onToggleFeatured = {
                            dbHelper.toggleFeatured(activePost)
                            refreshTick++
                        },
                        onPublishDraft = {
                            dbHelper.publishDraft(activePost.id)
                            refreshTick++
                        },
                        onEditPost = {
                            editingPost = activePost
                            selectedPostId = null
                        },
                        onDeletePost = {
                            dbHelper.deletePost(activePost.id)
                            selectedPostId = null
                            refreshTick++
                        },
                        onAddComment = { commentText ->
                            dbHelper.addComment(activePost.id, loggedInUser, commentText)
                            refreshTick++
                        }
                    )
                } else {
                    MainAppScreen(
                        currentUser = loggedInUser,
                        allUsers = users,
                        posts = posts,
                        totalCommentsCount = totalCommentsCount,
                        editingPost = editingPost,
                        darkTheme = darkTheme,
                        onToggleTheme = { darkTheme = !darkTheme },
                        onSwitchUser = { newUser ->
                            currentUser = newUser
                        },
                        onLogoutToLoginScreen = {
                            selectedPostId = null
                            editingPost = null
                            currentUser = null
                        },
                        onOpenPost = { post ->
                            selectedPostId = post.id
                        },
                        onSavePost = { post ->
                            dbHelper.savePost(post)
                            refreshTick++
                        },
                        onClearEditingPost = {
                            editingPost = null
                        },
                        onToggleLike = { post ->
                            dbHelper.toggleLike(post)
                            refreshTick++
                        },
                        onToggleBookmark = { post ->
                            dbHelper.toggleBookmark(post)
                            refreshTick++
                        },
                        onPublishDraft = { postId ->
                            dbHelper.publishDraft(postId)
                            refreshTick++
                        },
                        onResetDatabase = {
                            dbHelper.resetDatabase()
                            refreshTick++
                            val refreshedUsers = dbHelper.getAllUsers()
                            currentUser = refreshedUsers.firstOrNull()
                        }
                    )
                }
            }
        }
    }
}
