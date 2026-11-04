package com.waguri.inkpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waguri.inkpulse.model.AppUser
import com.waguri.inkpulse.model.ContentPost
import com.waguri.inkpulse.model.PostType
import com.waguri.inkpulse.model.UserRole
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    currentUser: AppUser,
    allUsers: List<AppUser>,
    posts: List<ContentPost>,
    totalCommentsCount: Int,
    editingPost: ContentPost?,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onSwitchUser: (AppUser) -> Unit,
    onLogoutToLoginScreen: () -> Unit,
    onOpenPost: (ContentPost) -> Unit,
    onSavePost: (ContentPost) -> Unit,
    onClearEditingPost: () -> Unit,
    onToggleLike: (ContentPost) -> Unit,
    onToggleBookmark: (ContentPost) -> Unit,
    onPublishDraft: (Long) -> Unit,
    onResetDatabase: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Jump to Write tab automatically when editing a post
    LaunchedEffect(editingPost) {
        if (editingPost != null) {
            selectedTab = 1
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(currentUser.avatarColor)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser.displayName.take(2).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = currentUser.displayName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Surface(
                                    color = if (currentUser.role == UserRole.CREATOR) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (currentUser.role == UserRole.CREATOR) "CREATOR" else "USER",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Offline SQLite • Tap Accounts to switch role",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                    IconButton(onClick = onLogoutToLoginScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Switch Demo Login"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Explore, contentDescription = null) },
                    label = { Text("Explore") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Create, contentDescription = null) },
                    label = {
                        Text(if (currentUser.role == UserRole.CREATOR) "Write Blog" else "Write Post")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = {
                        Text(if (currentUser.role == UserRole.CREATOR) "Studio" else "My Library")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.ManageAccounts, contentDescription = null) },
                    label = { Text("Accounts") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ExploreFeedTab(
                    posts = posts.filter { !it.isDraft },
                    onOpenPost = onOpenPost,
                    onToggleLike = onToggleLike,
                    onToggleBookmark = onToggleBookmark,
                    onJumpToWrite = { selectedTab = 1 }
                )
                1 -> WriteComposerTab(
                    currentUser = currentUser,
                    editingPost = editingPost,
                    onSavePost = { post, isDraft ->
                        onSavePost(post)
                        onClearEditingPost()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                if (isDraft) "Saved to offline drafts!" else "Published to offline feed!"
                            )
                        }
                        selectedTab = if (isDraft) 2 else 0
                    },
                    onCancelEdit = {
                        onClearEditingPost()
                    }
                )
                2 -> StudioAndLibraryTab(
                    currentUser = currentUser,
                    allPosts = posts,
                    onOpenPost = onOpenPost,
                    onPublishDraft = { draftId ->
                        onPublishDraft(draftId)
                        scope.launch {
                            snackbarHostState.showSnackbar("Draft is now live on the Explore feed!")
                        }
                    },
                    onToggleLike = onToggleLike,
                    onToggleBookmark = onToggleBookmark
                )
                3 -> AccountsAndDatabaseTab(
                    currentUser = currentUser,
                    allUsers = allUsers,
                    posts = posts,
                    totalCommentsCount = totalCommentsCount,
                    onSwitchUser = { newUser ->
                        onSwitchUser(newUser)
                        scope.launch {
                            snackbarHostState.showSnackbar("Switched demo session to ${newUser.displayName} (${newUser.role.label})")
                        }
                    },
                    onLogoutToLoginScreen = onLogoutToLoginScreen,
                    onResetDatabase = {
                        onResetDatabase()
                        scope.launch {
                            snackbarHostState.showSnackbar("Offline SQLite database reset to initial demo state!")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ExploreFeedTab(
    posts: List<ContentPost>,
    onOpenPost: (ContentPost) -> Unit,
    onToggleLike: (ContentPost) -> Unit,
    onToggleBookmark: (ContentPost) -> Unit,
    onJumpToWrite: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "Creator Blogs", "Community Posts", "Engineering", "Design", "Community Tips")

    val filteredPosts = posts.filter { post ->
        val matchesSearch = searchQuery.isBlank() ||
            post.title.contains(searchQuery, ignoreCase = true) ||
            post.subtitle.contains(searchQuery, ignoreCase = true) ||
            post.body.contains(searchQuery, ignoreCase = true) ||
            post.authorName.contains(searchQuery, ignoreCase = true) ||
            post.tags.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "All" -> true
            "Creator Blogs" -> post.postType == PostType.CREATOR_BLOG
            "Community Posts" -> post.postType == PostType.USER_CONTENT
            else -> post.category.equals(selectedFilter, ignoreCase = true)
        }
        matchesSearch && matchesFilter
    }

    val featuredBlogs = posts.filter { it.isFeatured && it.postType == PostType.CREATOR_BLOG }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text("Search blogs, community stories, #tags, authors...") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        // Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }

        // Featured Creator Blogs Section
        if (selectedFilter == "All" && searchQuery.isBlank() && featuredBlogs.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFFBBF24),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Featured Creator Blogs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                featuredBlogs.forEach { blog ->
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .clickable { onOpenPost(blog) },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(blog.coverColor)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    color = Color.White.copy(alpha = 0.22f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "FEATURED BLOG",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = "${blog.readingTimeMinutes} min read",
                                    color = Color.White.copy(alpha = 0.9f),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            Text(
                                text = blog.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = blog.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.88f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "By ${blog.authorName} (${blog.authorHandle})",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Latest Feed (${filteredPosts.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            AssistChip(
                onClick = onJumpToWrite,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                label = { Text("New Post") }
            )
        }

        filteredPosts.forEach { post ->
            PostFeedCard(
                post = post,
                onClick = { onOpenPost(post) },
                onToggleLike = { onToggleLike(post) },
                onToggleBookmark = { onToggleBookmark(post) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PostFeedCard(
    post: ContentPost,
    onClick: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleBookmark: () -> Unit,
    onPublishDraft: (() -> Unit)? = null
) {
    val isCreatorBlog = post.postType == PostType.CREATOR_BLOG
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = if (isCreatorBlog) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (isCreatorBlog) "CREATOR BLOG" else "COMMUNITY POST",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isCreatorBlog) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            },
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = "• ${post.category} • ${post.readingTimeMinutes}m read",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (post.isFeatured) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Featured",
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (post.subtitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(post.authorAvatarColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(2).uppercase(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (post.isDraft && onPublishDraft != null) {
                        Button(
                            onClick = onPublishDraft,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Publish,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Publish Now", style = MaterialTheme.typography.labelMedium)
                        }
                    } else {
                        IconButton(onClick = onToggleLike) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (post.isLiked) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "${post.likesCount}",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        IconButton(onClick = onToggleBookmark) {
                            Icon(
                                imageVector = if (post.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (post.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WriteComposerTab(
    currentUser: AppUser,
    editingPost: ContentPost?,
    onSavePost: (ContentPost, isDraft: Boolean) -> Unit,
    onCancelEdit: () -> Unit
) {
    val isCreator = currentUser.role == UserRole.CREATOR

    var title by remember(editingPost) { mutableStateOf(editingPost?.title ?: "") }
    var subtitle by remember(editingPost) { mutableStateOf(editingPost?.subtitle ?: "") }
    var body by remember(editingPost) { mutableStateOf(editingPost?.body ?: "") }
    var category by remember(editingPost) {
        mutableStateOf(
            editingPost?.category ?: if (isCreator) "Engineering" else "Community Tips"
        )
    }
    var tags by remember(editingPost) {
        mutableStateOf(editingPost?.tags ?: if (isCreator) "Android, Blog, Guide" else "Community, Discussion")
    }
    var isFeatured by remember(editingPost) { mutableStateOf(editingPost?.isFeatured ?: isCreator) }
    var selectedCoverColor by remember(editingPost) {
        mutableLongStateOf(
            editingPost?.coverColor ?: if (isCreator) 0xFF4F46E5L else 0xFF059669L
        )
    }

    val categories = if (isCreator) {
        listOf("Engineering", "Design", "Announcements", "Tutorials", "Deep Dive")
    } else {
        listOf("Community Tips", "Discussion", "User Story", "Showcase", "Q&A")
    }

    val coverColors = listOf(
        0xFF4F46E5L,
        0xFFDB2777L,
        0xFF059669L,
        0xFFD97706L,
        0xFF7C3AEDL,
        0xFF0284C7L
    )

    val wordCount = body.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
    val estimatedMinutes = ((wordCount / 130) + 1).coerceAtLeast(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Banner
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isCreator) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                }
            ),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null
                    )
                    Text(
                        text = if (editingPost != null) {
                            "Editing: ${editingPost.title}"
                        } else if (isCreator) {
                            "Creator Blog Studio (${currentUser.displayName})"
                        } else {
                            "Community Content Writer (${currentUser.displayName})"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isCreator) {
                        "Publishing as a Creator unlocks Featured Carousel placement, official blog badges, and full studio drafts."
                    } else {
                        "Share your thoughts, tutorials, or stories with the community. Everything saves immediately to the offline SQLite database."
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Category Selector
        Text(
            text = "Select Category",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                FilterChip(
                    selected = category == cat,
                    onClick = { category = cat },
                    label = { Text(cat) }
                )
            }
        }

        // Cover Accent Picker
        Text(
            text = "Header Accent Theme",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            coverColors.forEach { colorHex ->
                val isSelected = selectedCoverColor == colorHex
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(colorHex))
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { selectedCoverColor = colorHex }
                )
            }
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = {
                Text(if (isCreator) "Blog Headline / Title" else "Post Title")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        OutlinedTextField(
            value = subtitle,
            onValueChange = { subtitle = it },
            label = {
                Text("Subtitle or Short Summary")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        // Quick Markdown Formatting Bar
        Text(
            text = "Quick Formatting Snippets • $wordCount words (~$estimatedMinutes min read)",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = { body += "\n\n## Section Heading\n" },
                label = { Text("+ H2 Heading") }
            )
            AssistChip(
                onClick = { body += "\n\n### Subheading\n" },
                label = { Text("+ H3") }
            )
            AssistChip(
                onClick = { body += "\n- Key takeaway item" },
                label = { Text("+ Bullet List") }
            )
            AssistChip(
                onClick = { body += "\n\n> \"Highlight quote or callout here.\"\n" },
                label = { Text("+ Quote Block") }
            )
        }

        OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = {
                Text(if (isCreator) "Write your full blog post (Markdown supported)..." else "Write your community content...")
            },
            minLines = 8,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Tags (comma separated)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        if (isCreator) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Pin to Featured Creator Carousel",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Highlights this blog at the top of the Explore screen",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isFeatured,
                    onCheckedChange = { isFeatured = it }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (title.isNotBlank() && body.isNotBlank()) {
                        onSavePost(
                            ContentPost(
                                id = editingPost?.id ?: 0L,
                                authorId = currentUser.id,
                                authorName = currentUser.displayName,
                                authorHandle = "@${currentUser.username}",
                                authorRole = currentUser.role,
                                authorAvatarColor = currentUser.avatarColor,
                                postType = if (isCreator) PostType.CREATOR_BLOG else PostType.USER_CONTENT,
                                title = title.trim(),
                                subtitle = subtitle.trim(),
                                body = body.trim(),
                                category = category,
                                tags = tags.trim(),
                                readingTimeMinutes = estimatedMinutes,
                                isDraft = true,
                                isFeatured = isFeatured,
                                likesCount = editingPost?.likesCount ?: 0,
                                isLiked = editingPost?.isLiked ?: false,
                                isBookmarked = editingPost?.isBookmarked ?: false,
                                coverColor = selectedCoverColor
                            ),
                            true
                        )
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save Draft")
            }

            Button(
                onClick = {
                    if (title.isNotBlank() && body.isNotBlank()) {
                        onSavePost(
                            ContentPost(
                                id = editingPost?.id ?: 0L,
                                authorId = currentUser.id,
                                authorName = currentUser.displayName,
                                authorHandle = "@${currentUser.username}",
                                authorRole = currentUser.role,
                                authorAvatarColor = currentUser.avatarColor,
                                postType = if (isCreator) PostType.CREATOR_BLOG else PostType.USER_CONTENT,
                                title = title.trim(),
                                subtitle = subtitle.trim(),
                                body = body.trim(),
                                category = category,
                                tags = tags.trim(),
                                readingTimeMinutes = estimatedMinutes,
                                isDraft = false,
                                isFeatured = isFeatured,
                                likesCount = editingPost?.likesCount ?: 0,
                                isLiked = editingPost?.isLiked ?: false,
                                isBookmarked = editingPost?.isBookmarked ?: false,
                                coverColor = selectedCoverColor
                            ),
                            false
                        )
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Publish, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isCreator) "Publish Blog" else "Publish Post")
            }
        }

        if (editingPost != null) {
            OutlinedButton(
                onClick = onCancelEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel Editing")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StudioAndLibraryTab(
    currentUser: AppUser,
    allPosts: List<ContentPost>,
    onOpenPost: (ContentPost) -> Unit,
    onPublishDraft: (Long) -> Unit,
    onToggleLike: (ContentPost) -> Unit,
    onToggleBookmark: (ContentPost) -> Unit
) {
    var subTab by remember { mutableIntStateOf(0) }

    val myPublished = allPosts.filter { it.authorId == currentUser.id && !it.isDraft }
    val drafts = if (currentUser.role == UserRole.CREATOR) {
        allPosts.filter { it.isDraft }
    } else {
        allPosts.filter { it.authorId == currentUser.id && it.isDraft }
    }
    val bookmarked = allPosts.filter { it.isBookmarked && !it.isDraft }
    val totalLikesOnMyPosts = myPublished.sumOf { it.likesCount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Studio Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatSummaryCard(
                label = "Published",
                value = "${myPublished.size}",
                modifier = Modifier.weight(1f)
            )
            StatSummaryCard(
                label = "Offline Drafts",
                value = "${drafts.size}",
                modifier = Modifier.weight(1f)
            )
            StatSummaryCard(
                label = "Total Likes",
                value = "$totalLikesOnMyPosts",
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = subTab == 0,
                onClick = { subTab = 0 },
                label = { Text("My Published (${myPublished.size})") }
            )
            FilterChip(
                selected = subTab == 1,
                onClick = { subTab = 1 },
                label = { Text("Drafts (${drafts.size})") }
            )
            FilterChip(
                selected = subTab == 2,
                onClick = { subTab = 2 },
                label = { Text("Bookmarks (${bookmarked.size})") }
            )
        }

        val listToShow = when (subTab) {
            0 -> myPublished
            1 -> drafts
            else -> bookmarked
        }

        if (listToShow.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (subTab) {
                            0 -> "You haven't published any posts under ${currentUser.displayName} yet."
                            1 -> "No offline drafts waiting right now."
                            else -> "Tap the bookmark icon on any blog or community story to save it here."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            listToShow.forEach { post ->
                PostFeedCard(
                    post = post,
                    onClick = { onOpenPost(post) },
                    onToggleLike = { onToggleLike(post) },
                    onToggleBookmark = { onToggleBookmark(post) },
                    onPublishDraft = if (post.isDraft) {
                        { onPublishDraft(post.id) }
                    } else null
                )
            }
        }
    }
}

@Composable
private fun StatSummaryCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AccountsAndDatabaseTab(
    currentUser: AppUser,
    allUsers: List<AppUser>,
    posts: List<ContentPost>,
    totalCommentsCount: Int,
    onSwitchUser: (AppUser) -> Unit,
    onLogoutToLoginScreen: () -> Unit,
    onResetDatabase: () -> Unit
) {
    val creatorBlogsCount = posts.count { it.postType == PostType.CREATOR_BLOG && !it.isDraft }
    val userStoriesCount = posts.count { it.postType == PostType.USER_CONTENT && !it.isDraft }
    val draftsCount = posts.count { it.isDraft }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Active Session Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Active Demo Session",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${currentUser.displayName} (@${currentUser.username})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Role: ${currentUser.role.label} • ${currentUser.email}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = currentUser.bio,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                )
            }
        }

        // Instant One-Tap Role & Account Switcher
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SwitchAccount,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = "Instant Demo Switcher",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap any account below to test Creator vs. Community Writer views immediately",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        allUsers.forEach { account ->
            val isCurrent = account.id == currentUser.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSwitchUser(account) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(account.avatarColor)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = account.displayName.take(2).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = account.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "@${account.username} • ${account.role.label}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        color = if (isCurrent) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isCurrent) "ACTIVE" else "SWITCH",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // Offline SQLite Inspector Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Offline SQLite Database Inspector (inkpulse_offline.db)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Demo Accounts stored: ${allUsers.size}\n" +
                        "• Published Creator Blogs: $creatorBlogsCount\n" +
                        "• Published Community Posts: $userStoriesCount\n" +
                        "• Saved Offline Drafts: $draftsCount\n" +
                        "• Offline Discussion Comments: $totalCommentsCount",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onResetDatabase,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset Demo DB")
                    }
                    Button(
                        onClick = onLogoutToLoginScreen,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Login Screen")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
