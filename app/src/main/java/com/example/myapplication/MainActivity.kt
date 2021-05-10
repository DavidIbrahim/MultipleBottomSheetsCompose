package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.BottomSheetShape
import com.example.myapplication.ui.theme.BottomSheetWithCloseDialog
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                                        MainLayout()

                }
            }
        }
    }

}
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainLayout() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var currentBottomSheet: BottomSheetScreen? by remember{
        mutableStateOf(null)
    }

    if(scaffoldState.bottomSheetState.isCollapsed)
        currentBottomSheet = null


    val closeSheet: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.collapse()

        }
    }


    val openSheet: (BottomSheetScreen) -> Unit = {
        scope.launch {
            currentBottomSheet = it
            scaffoldState.bottomSheetState.expand() }

    }

    BottomSheetScaffold(sheetPeekHeight = 0.dp, scaffoldState = scaffoldState,
        sheetShape = BottomSheetShape,
        sheetContent = {
            currentBottomSheet?.let { currentSheet ->
                SheetLayout(currentSheet,closeSheet)
            }
        }) { paddingValues ->
            Box(Modifier.padding(paddingValues)){
                MainContent(openSheet)
            }
    }
}

@Composable
fun MainContent(openSheet: (BottomSheetScreen) -> Unit) {
    Column(Modifier.fillMaxSize(),verticalArrangement = Arrangement.SpaceEvenly,horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "This is Main Content")
        Button(onClick = { openSheet(BottomSheetScreen.Screen1) }) {
            Text(text = "Open bottom sheet 1")
        }

        Button(onClick = { openSheet(BottomSheetScreen.Screen2) }) {
            Text(text = "Open bottom sheet 2")
        }
        Button(onClick = { openSheet(BottomSheetScreen.Screen3("this is an argument")) }) {
            Text(text = "Open bottom sheet 2")
        }
    }
}

@Composable
fun SheetLayout(currentScreen: BottomSheetScreen,onCloseBottomSheet :()->Unit) {
    BottomSheetWithCloseDialog(onCloseBottomSheet){
        when(currentScreen){
            BottomSheetScreen.Screen1 -> Screen1()
            BottomSheetScreen.Screen2 -> Screen2()
            is BottomSheetScreen.Screen3 -> Screen3(argument = currentScreen.argument)
        }

    }
}

@Composable
fun Screen1() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Yellow, shape = RectangleShape)){
        Text(text = "This is bottom screen 1", Modifier.align(Alignment.Center).padding(16.dp),color = Color.Black,
        fontSize = 15.sp)
    }
}
@Composable
fun Screen2() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Cyan, shape = RectangleShape)){
        Text(text = "This is bottom screen 2", Modifier.align(Alignment.Center).padding(16.dp),color = Color.Black,fontSize = 15.sp)
    }

}@Composable
fun Screen3(argument: String) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray, shape = RectangleShape)){
        Text(text = "This is bottom screen 3 and $argument", Modifier.align(Alignment.Center).padding(16.dp),color = Color.Black,fontSize = 15.sp)
    }
}


sealed class BottomSheetScreen() {
    object Screen1: BottomSheetScreen()
    object Screen2: BottomSheetScreen()
    class Screen3(val argument:String):BottomSheetScreen()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}