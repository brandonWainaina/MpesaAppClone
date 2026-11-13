package com.example.mpesaappclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SendGreen = Color(0xFF48C273)
private val PayBlue = Color(0xFF1E7CF8)
private val WithdrawRed = Color(0xFFF82E5B)
private val AirtimeBlue = Color(0xFF31C5FB)
private val DarkGreen = Color(0xFF087A45)
private val PageBackground = Color(0xFFFFFFFF)
private val GreyText = Color(0xFF929398)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MpesaApp()
            }
        }
    }
}

sealed class Screen {
    object SendAndRequest : Screen()
    object Pay : Screen()
    object withdraw : screen()
    object airtime : screen()
}

@Composable
fun MpesaApp() {
    var loggedIn by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var currentScreen by remember { mutableStateOf<Screen?>(null) }

    if (!loggedIn) {
        WelcomeScreen(onContinue = { loggedIn = true })
    } else {
                when (currentScreen) {
            Screen.SendAndRequest -> SendAndRequestScreen(
                onBack = { currentScreen = null }
            )
            Screen.Pay -> PayScreen(
                onBack = { currentScreen = null }
            )
            screen.withdraw -> withdrawscreen(
                onBack = { currentscreen = null }
            )
            screen.airtime -> airtimescreen(
                onBack = { currentscreen = null }
            )
            null -> MainScreen(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onNavigate = { currentScreen = it }
            )
        }
    }
}

@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkGreen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(Modifier.height(70.dp))
                Text(
                    "M-PESA",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(18.dp))
                Text(
                    "Welcome to M-PESA",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Your money. Your world. Simple, secure and convenient.",
                    color = Color.White.copy(alpha = 0.88f),
                    fontSize = 16.sp
                )
            }
            Column {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = DarkGreen
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("OPEN DEMO", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(18.dp))
                Text(
                    "Unofficial educational demo — not a Safaricom service",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        containerColor = PageBackground,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF9FCFA),
                tonalElevation = 5.dp
            ) {
                val labels = listOf("Home", "Transact", "Services", "Grow")
                val icons = listOf(
                    Icons.Default.Home,
                    Icons.Default.SwapHoriz,
                    Icons.Default.List,
                    Icons.Default.ShowChart
                )

                labels.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        icon = {
                            Icon(
                                icons[index],
                                contentDescription = label,
                                modifier = Modifier.size(27.dp)
                            )
                        },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SendGreen,
                            selectedTextColor = SendGreen,
                            unselectedIconColor = Color(0xFF969B98),
                            unselectedTextColor = Color(0xFF969B98),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { padding ->
                when (selectedTab) {
            0 -> HomeScreen(Modifier.padding(padding), onNavigate = onNavigate)
            1 -> SimplePage("Transact", Modifier.padding(padding))
            2 -> SimplePage("Services", Modifier.padding(padding))
            else -> SimplePage("Grow", Modifier.padding(padding))
        }
      }
    }

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onNavigate: (Screen) -> Unit) {
    var showBalance by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        // Profile and greeting
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(Color(0xFFD8F0E1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = DarkGreen,
                    modifier = Modifier.size(34.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text("Good Morning", fontSize = 17.sp, color = Color.Black)
                Text(
                    "Wainaina 👋",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            IconButton(onClick = {}) {
                Icon(Icons.Default.NotificationsNone, "Notifications",
                    tint = Color.Black, modifier = Modifier.size(27.dp))
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.PieChart, "Statistics",
                    tint = Color.Black, modifier = Modifier.size(27.dp))
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.QrCodeScanner, "QR code",
                    tint = Color.Black, modifier = Modifier.size(27.dp))
            }
        }

        Spacer(Modifier.height(30.dp))

        // White balance card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 190.dp)
                .shadow(2.dp, RoundedCornerShape(30.dp)),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 17.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Balance",
                    fontSize = 21.sp,
                    color = Color.Black
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        if (showBalance) "KSH. 175.00" else "KSH. •••••",
                        fontSize = 34.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { showBalance = !showBalance }) {
                        Icon(
                            if (showBalance) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = "Toggle balance",
                            tint = Color.Black,
                            modifier = Modifier.size(27.dp)
                        )
                    }
                }

                Text(
                    "Available FULIZA: KSH 800.00",
                    color = PayBlue,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        Spacer(Modifier.height(30.dp))

        // Quick action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
               
            ActionButton(
                "SEND AND\nREQUEST", "↗", SendGreen, Modifier.weight(1f),
                onClick = { onNavigate(Screen.SendAndRequest) }
            )
            ActionButton(
                "PAY", "▣", PayBlue, Modifier.weight(1f),
                onClick = { onNavigate(Screen.Pay) }
            )
                        ActionButton(
                "WITHDRAW", "＄", WithdrawRed, Modifier.weight(1f),
                onClick = { onNavigate(Screen.Withdraw) }
            )
            ActionButton(
                "AIRTIME", "▤", AirtimeBlue, Modifier.weight(1f),
                onClick = { onNavigate(Screen.Airtime) }
            )       
            }

        // Statements heading
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "M-PESA STATEMENTS",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                "SEE ALL",
                color = SendGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(Modifier.height(18.dp))

        // Example transaction
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(Color(0xFFD5F3E0), CircleShape)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text("0700000000", color = GreyText, fontSize = 16.sp)
                Text("Demo transaction", color = GreyText, fontSize = 11.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("-KSH. 100.00", color = Color.Black, fontSize = 17.sp)
                Text("18 Jun, 3:32 PM", color = GreyText, fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Original five-image promotional carousel
        PromoCarousel()

        // Financial services
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 25.dp),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Financial Services",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "View All",
                        color = SendGreen,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(18.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = Modifier.size(35.dp)
                    )
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("M-Shwari", fontWeight = FontWeight.Bold)
                        Text(
                            "Save and borrow",
                            color = GreyText,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Text(
            "Unofficial educational demo — not a Safaricom service",
            color = GreyText,
            fontSize = 10.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButton(
    label: String,
    symbol: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .background(color, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                symbol,
                color = Color.White,
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            label,
            fontSize = 13.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun AirtimeScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "Buy Airtime",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                "Airtime UI goes here.",
                fontSize = 16.sp,
                color = GreyText
            )
        }
    }
}

@Composable
fun WithdrawScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "Withdraw",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                "Withdraw UI goes here.",
                fontSize = 16.sp,
                color = GreyText
            )
        }
    }
}

@Composable
fun PayScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "Pay",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                "Pay UI goes here.",
                fontSize = 16.sp,
                color = GreyText
            )
        }
    }
}

@Composable
fun SendAndRequestScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "Send and Request",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                "Send & request UI goes here.",
                fontSize = 16.sp,
                color = GreyText
            )
        }
    }
}

@Composable
fun SimplePage(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PromoCarousel() {
    val slides = listOf(
        R.drawable.promo_pic1,
        R.drawable.promo_pic2,
        R.drawable.promo_pic3,
        R.drawable.promo_pic4,
        R.drawable.promo_pic5
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            selectedIndex = (selectedIndex + 1) % slides.size
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = slides[selectedIndex]),
            contentDescription = "Promotional slide ${selectedIndex + 1}",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            slides.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(10.dp)
                        .background(
                            if (index == selectedIndex)
                                Color.Red
                            else
                                Color.Gray,
                            CircleShape
                        )
                )
            }
        }
    }
}
