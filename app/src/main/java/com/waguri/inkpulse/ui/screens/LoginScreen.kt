package com.waguri.inkpulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waguri.inkpulse.model.AppUser
import com.waguri.inkpulse.model.UserRole

@Composable
fun LoginScreen(
    demoUsers: List<AppUser>,
    onSelectUser: (AppUser) -> Unit,
    onCreateCustomUser: (displayName: String, username: String, email: String, role: UserRole, bio: String) -> Unit
) {
    var showCustomForm by remember { mutableStateOf(false) }
    var customName by remember { mutableStateOf("") }
    var customHandle by remember { mutableStateOf("") }
    var customEmail by remember { mutableStateOf("") }
    var customBio by remember { mutableStateOf("") }
    var customRole by remember { mutableStateOf(UserRole.CREATOR) }

    val creatorAccounts = demoUsers.filter { it.role == UserRole.CREATOR }
    val userAccounts = demoUsers.filter { it.role == UserRole.USER }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // App Header Banner
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "InkPulse Studio",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Creator Blog & Community Content Hub",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Offline SQLite DB Active • Tap any demo profile below to test!",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Creator Demo Logins Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = "Demo Creator Logins",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Write official blogs, manage drafts, pin featured posts & view studio stats",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        creatorAccounts.forEach { account ->
            DemoAccountCard(
                user = account,
                onClick = { onSelectUser(account) }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Community User Demo Logins Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Column {
                Text(
                    text = "Demo Community User Logins",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Publish community stories, tips, discussions, bookmark blogs & comment",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        userAccounts.forEach { account ->
            DemoAccountCard(
                user = account,
                onClick = { onSelectUser(account) }
            )
        }

        // Custom Demo Account Creator
        OutlinedButton(
            onClick = { showCustomForm = !showCustomForm },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (showCustomForm) "Hide Custom Demo Account Form" else "Create Custom Demo Account")
        }

        if (showCustomForm) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "New Offline Demo Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = customRole == UserRole.CREATOR,
                            onClick = { customRole = UserRole.CREATOR },
                            label = { Text("Role: Creator (Blog Studio)") }
                        )
                        FilterChip(
                            selected = customRole == UserRole.USER,
                            onClick = { customRole = UserRole.USER },
                            label = { Text("Role: User (Content Writer)") }
                        )
                    }

                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("Display Name (e.g. Yash Daive)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = customHandle,
                        onValueChange = { customHandle = it },
                        label = { Text("Username Handle (e.g. waguri_dev)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = customEmail,
                        onValueChange = { customEmail = it },
                        label = { Text("Email (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = customBio,
                        onValueChange = { customBio = it },
                        label = { Text("Short Bio") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (customName.isNotBlank() || customHandle.isNotBlank()) {
                                onCreateCustomUser(
                                    customName.ifBlank { customHandle },
                                    customHandle.ifBlank { customName },
                                    customEmail,
                                    customRole,
                                    customBio
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Login, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save to Offline DB & Sign In")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DemoAccountCard(
    user: AppUser,
    onClick: () -> Unit
) {
    val isCreator = user.role == UserRole.CREATOR
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(user.avatarColor)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.displayName.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = user.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        color = if (isCreator) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (isCreator) "CREATOR" else "USER",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isCreator) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = "@${user.username} • ${user.badge}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Login,
                    contentDescription = "Login",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}
