package com.waguri.inkpulse.model

enum class UserRole(val label: String) {
    CREATOR("Creator"),
    USER("Community Writer")
}

enum class PostType(val label: String) {
    CREATOR_BLOG("Creator Blog"),
    USER_CONTENT("Community Post")
}

data class AppUser(
    val id: Long,
    val username: String,
    val displayName: String,
    val email: String,
    val passwordHint: String,
    val role: UserRole,
    val bio: String,
    val badge: String,
    val avatarColor: Long
)

data class ContentPost(
    val id: Long = 0L,
    val authorId: Long,
    val authorName: String,
    val authorHandle: String,
    val authorRole: UserRole,
    val authorAvatarColor: Long,
    val postType: PostType,
    val title: String,
    val subtitle: String,
    val body: String,
    val category: String,
    val tags: String,
    val readingTimeMinutes: Int,
    val isDraft: Boolean = false,
    val isFeatured: Boolean = false,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val coverColor: Long = 0xFF4F46E5,
    val createdAt: Long = System.currentTimeMillis()
)

data class CommentItem(
    val id: Long = 0L,
    val postId: Long,
    val authorName: String,
    val authorHandle: String,
    val authorRole: UserRole,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)
