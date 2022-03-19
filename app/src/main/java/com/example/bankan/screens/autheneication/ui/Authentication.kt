package com.example.bankan.screens.autheneication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bankan.R
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.autheneication.viewmodel.*


@Preview(showBackground = true)
@Composable
fun Authentication() {
    val viewModel: AuthenticationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val uiState by viewModel.uiState.collectAsState()
    BankanTheme {
        AuthenticationContent(
            modifier = Modifier.fillMaxWidth(),
            authenticationState = uiState,
            handleEvent = viewModel::handleEvent
        )
    }
}

@Composable
fun AuthenticationContent(
    modifier: Modifier = Modifier,
    authenticationState: AuthenticationState,
    handleEvent: (event: AuthenticationEvent) -> Unit
) {
    BankanTheme {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            if (authenticationState.isLoading) {
                CircularProgressIndicator()
            } else {

                AuthenticationForm(
                    modifier = Modifier.fillMaxSize(),
                    authenticationMode = authenticationState.authenticationMode,
                    nickname = authenticationState.nickname,
                    email = authenticationState.email,
                    password = authenticationState.password,
                    completedPasswordRequirements = authenticationState.passwordRequirements,
                    enableAuthentication = authenticationState.isFormValid(),
                    onNicknameChanged = { nickname ->
                        handleEvent(AuthenticationEvent.NicknameChanged(nickname))
                    },
                    onEmailChanged = { email ->
                        handleEvent(AuthenticationEvent.EmailChanged(email))
                    },
                    onPasswordChanged = { password ->
                        handleEvent(AuthenticationEvent.PasswordChanged(password))
                    },
                    onAuthenticate = {
                        handleEvent(AuthenticationEvent.Authenticate)
                    },
                    onToggleMode = {
                        handleEvent(AuthenticationEvent.ChangeAuthenticationMode(if (authenticationState.authenticationMode == AuthenticationMode.SIGN_IN) AuthenticationMode.SIGN_UP else AuthenticationMode.SIGN_IN))
                    },
                    onContinueAsGuest = {
                        handleEvent(AuthenticationEvent.ChangeAuthenticationMode(AuthenticationMode.GUEST))
                    },
                )

                authenticationState.error?.let { error ->
                    AuthenticationErrorDialog(
                        error = error,
                        dismissError = {
                            handleEvent(
                                AuthenticationEvent.ErrorDismissed
                            )
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    nickname: String,
    email: String,
    password: String,
    completedPasswordRequirements: List<PasswordRequirements>,
    enableAuthentication: Boolean,
    onNicknameChanged: (nickname: String) -> Unit,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onAuthenticate: () -> Unit,
    onToggleMode: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    BankanTheme {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            AuthenticationTitle(
                authenticationMode = authenticationMode
            )
            Spacer(modifier = Modifier.height(40.dp))
            val emailFocusRequester = FocusRequester()
            val passwordFocusRequester = FocusRequester()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(visible = authenticationMode != AuthenticationMode.SIGN_IN) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NicknameInput(
                                modifier = Modifier.fillMaxWidth(),
                                nickname = nickname,
                                onNicknameChanged = onNicknameChanged,
                                onNextClicked = { emailFocusRequester.requestFocus() }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    AnimatedVisibility(visible = authenticationMode != AuthenticationMode.GUEST) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            EmailInput(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(emailFocusRequester),
                                email = email,
                                onEmailChanged = onEmailChanged,
                                onNextClicked = { passwordFocusRequester.requestFocus() }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            PasswordInput(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(passwordFocusRequester),
                                password = password,
                                onPasswordChanged = onPasswordChanged,
                                onDoneClicked = onAuthenticate
                            )
                        }
                    }
                    AnimatedVisibility(visible = authenticationMode == AuthenticationMode.SIGN_UP) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(12.dp))
                            PasswordRequirementsUI(satisfiedRequirements = completedPasswordRequirements)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthenticationButton(
                        authenticationMode = authenticationMode,
                        enableAuthentication = enableAuthentication,
                        onAuthenticate = onAuthenticate
                    )

                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ToggleAuthenticationMode(
                modifier = Modifier.fillMaxWidth(),
                authenticationMode = authenticationMode,
                toggleAuthentication = {
                    onToggleMode()
                },
                continueAsGuest = {
                    onContinueAsGuest()
                }
            )
        }
    }
}


@Composable
fun AuthenticationTitle(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode
) {
    BankanTheme {
        Text(
            modifier = modifier,
            text = stringResource(
                if (authenticationMode == AuthenticationMode.SIGN_IN) {
                    R.string.label_sign_in_to_account
                } else {
                    R.string.label_sign_up_for_account
                }
            ),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
fun AuthenticationErrorDialog(
    modifier: Modifier = Modifier,
    error: String,
    dismissError: () -> Unit
) {
    BankanTheme {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                dismissError()
            },
            buttons = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(
                        onClick = {
                            dismissError()
                        }
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.error_action
                            )
                        )
                    }
                }
            },
            title = {
                Text(
                    text = stringResource(
                        id = R.string.error_title
                    ),
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = error
                )
            }
        )
    }
}

@Composable
fun PasswordRequirementsUI(
    modifier: Modifier = Modifier,
    satisfiedRequirements: List<PasswordRequirements>
) {
    BankanTheme {
        Column(
            modifier = modifier
        ) {
            PasswordRequirements.values().forEach { requirement ->
                Requirement(
                    message = stringResource(
                        id = requirement.label
                    ),
                    satisfied = satisfiedRequirements.contains(
                        requirement
                    )
                )
            }
        }
    }
}

@Composable
fun Requirement(
    modifier: Modifier = Modifier,
    message: String,
    satisfied: Boolean
) {
    BankanTheme {
        val tint = if (satisfied) {
            MaterialTheme.colors.primary
        } else MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
        val requirementStatus = if (satisfied) {
            stringResource(
                id =
                R.string.password_requirement_satisfied, message
            )
        } else {
            stringResource(
                id =
                R.string.password_requirement_needed, message
            )
        }

        Row(
            modifier = modifier
                .padding(6.dp)
                .semantics(mergeDescendants = true) {
                    text = AnnotatedString(requirementStatus)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = tint
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.clearAndSetSemantics { },
                text = message,
                fontSize = 12.sp,
                color = tint
            )
        }
    }
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    password: String?,
    onPasswordChanged: (email: String) -> Unit,
    onDoneClicked: () -> Unit
) {
    BankanTheme {

        var isPasswordHidden by remember {
            mutableStateOf(true)
        }
        TextField(
            modifier = modifier,
            value = password ?: "",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = {
                onDoneClicked()
            }),
            onValueChange = {
                onPasswordChanged(it)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable(
                        onClickLabel = if (isPasswordHidden) {
                            stringResource(
                                id =
                                R.string.cd_show_password
                            )
                        } else stringResource(
                            id =
                            R.string.cd_hide_password
                        )
                    ) {
                        isPasswordHidden = !isPasswordHidden
                    },
                    imageVector = if (isPasswordHidden) {
                        Icons.Default.Visibility
                    } else Icons.Default.VisibilityOff,
                    contentDescription = null
                )
            },
            label = {
                Text(text = stringResource(id = R.string.label_password))
            },
            visualTransformation = if (isPasswordHidden) {
                PasswordVisualTransformation()
            } else VisualTransformation.None
        )
    }
}

@Composable
fun NicknameInput(
    modifier: Modifier = Modifier,
    nickname: String,
    onNicknameChanged: (email: String) -> Unit,
    onNextClicked: () -> Unit
) {
    BankanTheme {
        TextField(
            modifier = modifier,
            value = nickname,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNextClicked()
                }
            ),
            onValueChange = { newNickname ->
                onNicknameChanged(newNickname)
            },
            label = {
                Text(
                    text = stringResource(R.string.label_nickname)
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChanged: (email: String) -> Unit,
    onNextClicked: () -> Unit
) {
    BankanTheme {

        TextField(
            modifier = modifier,
            value = email,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    onNextClicked()
                }
            ),
            onValueChange = { newEmail ->
                onEmailChanged(newEmail)
            },
            label = {
                Text(
                    text = stringResource(
                        id = R.string.label_email
                    )
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
fun AuthenticationButton(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    enableAuthentication: Boolean,
    onAuthenticate: () -> Unit
) {
    BankanTheme {
        Button(
            modifier = modifier,
            onClick = {
                onAuthenticate()
            },
            enabled = enableAuthentication
        ) {
            Text(
                text = stringResource(
                    when (authenticationMode) {
                        AuthenticationMode.SIGN_IN -> {
                            R.string.action_sign_in
                        }
                        AuthenticationMode.SIGN_UP -> {
                            R.string.action_sign_up
                        }
                        AuthenticationMode.GUEST -> {
                            R.string.continue_as_guest
                        }
                    }
                )
            )
        }
    }
}

@Composable
fun ToggleAuthenticationMode(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    continueAsGuest: () -> Unit,
    toggleAuthentication: () -> Unit
) {
    BankanTheme {
        Surface(
            modifier = modifier
                .background(MaterialTheme.colors.surface),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedVisibility(visible = authenticationMode != AuthenticationMode.GUEST) {
                    TextButton(
                        modifier = Modifier,
                        onClick = { continueAsGuest() }
                    ) {
                        Text(
                            text = stringResource(R.string.continue_as_guest),
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
                TextButton(
                    modifier = Modifier,
                    onClick = { toggleAuthentication() }
                ) {
                    Text(
                        text = stringResource(
                            when (authenticationMode) {
                                AuthenticationMode.SIGN_IN ->
                                    R.string.action_need_account
                                AuthenticationMode.SIGN_UP ->
                                    R.string.action_already_have_account
                                AuthenticationMode.GUEST ->
                                    R.string.action_already_have_account
                            }
                        ),
                    style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}

