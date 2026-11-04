package com.waguri.inkpulse.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waguri.inkpulse.model.AppUser
import com.waguri.inkpulse.model.CommentItem
import com.waguri.inkpulse.model.ContentPost
import com.waguri.inkpulse.model.PostType
import com.waguri.inkpulse.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: ContentPost,
    currentUser: AppUser,
    comments: List<CommentItem>,
    onBack: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleFeatured: () -> Unit,
    onPublishDraft: () -> Unit,
    onEditPost: () -> Unit,
    onDeletePost: () -> Unit,
    onAddComment: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }
    val canManage = currentUser.id == post.authorId || currentUser.role == UserRole.CREATOR

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (post.postType == PostType.CREATOR_BLOG) "Creator Blog" else "Community Story",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (currentUser.role == UserRole.CREATOR) {
                        IconButton(onClick = onToggleFeatured) {
                            Icon(
                                imageVector = if (post.isFeatured) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Toggle Featured",
                                tint = if (post.isFeatured) Color(0xFFFBBF24) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    IconButton(onClick = onToggleBookmark) {
                        Icon(
                            imageVector = if (post.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (post.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (canManage) {
                        IconButton(onClick = onEditPost) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = onDeletePost) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(post.coverColor))
                    .padding(22.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = post.category.uppercase(),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            color = Color.Black.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${post.readingTimeMinutes} min read",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        if (post.isDraft) {
                            Surface(
                                color = Color(0xFFF59E0B),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "DRAFT",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = post.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    if (post.subtitle.isNotBlank()) {
                        Text(
                            text = post.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Draft publish banner if draft
                if (post.isDraft && canManage) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "This post is saved as an offline draft",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Publish it to make it visible in the main feed.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Button(onClick = onPublishDraft) {
                                Icon(imageVector = Icons.Default.Publish, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Publish")
                            }
                        }
                    }
                }

                // Author Row + Like button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(post.authorAvatarColor)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = post.authorName.take(2).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = post.authorName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    color = if (post.authorRole == UserRole.CREATOR) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = post.authorRole.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = post.authorHandle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onToggleLike,
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("${post.likesCount}")
                    }
                }

                HorizontalDivider()

                // Formatted Article Body
                FormattedMarkdownBody(body = post.body)

                if (post.tags.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        post.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { tag ->
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "#$tag",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Comments Section
                Text(
                    text = "Offline Discussion (${comments.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = {
                            Text("Comment as ${currentUser.displayName} (${currentUser.role.label})...")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                onAddComment(commentText)
                                commentText = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Comment",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                comments.forEach { comment ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = comment.authorName,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    color = if (comment.authorRole == UserRole.CREATOR) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    },
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (comment.authorRole == UserRole.CREATOR) "CREATOR" else "USER",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                                Text(
                                    text = comment.authorHandle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = comment.content,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun FormattedMarkdownBody(body: String) {
    val lines = body.lines()
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        lines.forEach { rawLine ->
            val line = rawLine.trim()
            when {
                line.isEmpty() -> Spacer(modifier = Modifier.height(2.dp))
                line.startsWith("## ") -> {
                    Text(
                        text = line.removePrefix("## "),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                line.startsWith("### ") -> {
                    Text(
                        text = line.removePrefix("### "),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                line.startsWith("> ") -> {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = line.removePrefix("> "),
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
                line.startsWith("- ") -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "•",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                        Text(
                            text = line.removePrefix("- "),
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp
                        )
                    }
                }
                else -> {
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 25.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
