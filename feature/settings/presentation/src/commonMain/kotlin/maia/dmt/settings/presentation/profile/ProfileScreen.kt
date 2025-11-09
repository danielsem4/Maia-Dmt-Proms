package maia.dmt.settings.presentation.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmtproms.feature.settings.presentation.generated.resources.Res
import dmtproms.feature.settings.presentation.generated.resources.settings_profile
import dmtproms.feature.settings.presentation.generated.resources.settings_profile_clinic
import dmtproms.feature.settings.presentation.generated.resources.settings_profile_email
import dmtproms.feature.settings.presentation.generated.resources.settings_profile_phone
import maia.dmt.core.designsystem.components.layouts.DmtBaseScreen
import maia.dmt.core.designsystem.theme.DmtTheme
import maia.dmt.core.domain.dto.Module
import maia.dmt.core.domain.dto.User
import maia.dmt.core.presentation.util.ObserveAsEvents
import maia.dmt.settings.presentation.components.DmtSettingsSection
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ProfileEvent.NavigateBack -> onNavigateBack()
        }
    }

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    DmtBaseScreen(
        titleText = stringResource(Res.string.settings_profile),
        onIconClick = { onAction(ProfileAction.OnBackClick) },
        content = {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                state.user != null -> {
                    ProfileContent(
                        user = state.user,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    )
}

@Composable
private fun ProfileContent(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        ProfileHeader(user = user)

        Spacer(modifier = Modifier.height(32.dp))

        DmtSettingsSection {
            if (user.email != null) {
                ProfileInfoRow(
                    label = stringResource(Res.string.settings_profile_email),
                    value = user.email!!
                )
            }

            if (user.email != null && user.phone_number != null) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            if (user.phone_number != null) {
                ProfileInfoRow(
                    label =stringResource(Res.string.settings_profile_phone),
                    value = user.phone_number!!
                )
            }

            if (user.clinicName != null && (user.email != null || user.phone_number != null)) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            if (user.clinicName != null) {
                ProfileInfoRow(
                    label = stringResource(Res.string.settings_profile_clinic),
                    value = user.clinicName!!
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getInitials(user.first_name, user.last_name),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${user.first_name.orEmpty()} ${user.last_name.orEmpty()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "ID: ${user.id}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String,
    icon: DrawableResource? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = vectorResource(icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

private fun getInitials(firstName: String?, lastName: String?): String {
    val first = firstName?.firstOrNull()?.uppercase() ?: ""
    val last = lastName?.firstOrNull()?.uppercase() ?: ""
    return "$first$last".ifEmpty { "U" }
}

@Composable
@Preview
fun ProfilePreview() {
    DmtTheme {
        ProfileScreen(
            state = ProfileState(
                user = User(
                    id = 123456,
                    email = "john.doe@example.com",
                    phone_number = "+1 234 567 8900",
                    first_name = "John",
                    last_name = "Doe",
                    is_doctor = true,
                    is_patient = false,
                    is_clinic_manager = true,
                    clinicName = "Central Medical Clinic",
                    modules = listOf(
                        Module(module_id = 1, module_name = "Cardiology"),
                        Module(module_id = 2, module_name = "Radiology"),
                        Module(module_id = 3, module_name = "Laboratory")
                    )
                )
            ),
            onAction = {}
        )
    }
}

@Composable
@Preview
fun ProfilePreviewDark() {
    DmtTheme(darkTheme = true) {
        ProfileScreen(
            state = ProfileState(
                user = User(
                    id = 123456,
                    email = "john.doe@example.com",
                    phone_number = "+1 234 567 8900",
                    first_name = "John",
                    last_name = "Doe",
                    is_doctor = true,
                    is_patient = false,
                    clinicName = "Central Medical Clinic",
                    modules = listOf(
                        Module(module_id = 1, module_name = "Cardiology"),
                        Module(module_id = 2, module_name = "Radiology")
                    )
                )
            ),
            onAction = {}
        )
    }
}