package com.wwa.tictactoe.present

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.wwa.tictactoe.R
import com.wwa.tictactoe.databinding.ActivityGameBinding
import com.wwa.tictactoe.utility.NetworkChangeListener

class GameActivity : AppCompatActivity() {

    // Network change listener
    private val networkChangeListener: NetworkChangeListener = NetworkChangeListener()
    private lateinit var binding: ActivityGameBinding

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private var boardList = mutableListOf<Button>()

    enum class Turn {
        NOUGHT, CROSS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize the game board
        initBoard()
    }

    private fun initBoard() {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }


    /**
     * Called when a board button is tapped. Handles the player's move, checks for victory conditions,
     * and displays the result in a dialog.
     */
    fun boardTapped(view: View) {

        if (view !is Button)
            return
        addToBoard(view)

        if (checkForVictory(NOUGHT)) {
            noughtsScore++
            result("Noughts Win!")
        }

        if (checkForVictory(CROSS)) {
            crossesScore++
            result("Crosses Win!")
        }

        if (fullBoard()) {
            result("None")
        }

    }

    /**
     * Checks if a player with the specified symbol has won the game.
     */
    private fun checkForVictory(s: String): Boolean {

        return when {
            // Horizontal Victory
            (match(binding.a1, s) && match(binding.a2, s) && match(binding.a3, s)) ||
                    (match(binding.b1, s) && match(binding.b2, s) && match(binding.b3, s)) ||
                    (match(binding.c1, s) && match(binding.c2, s) && match(binding.c3, s)) -> true

            // Vertical Victory
            (match(binding.a1, s) && match(binding.b1, s) && match(binding.c1, s)) ||
                    (match(binding.a2, s) && match(binding.b2, s) && match(binding.c2, s)) ||
                    (match(binding.a3, s) && match(binding.b3, s) && match(binding.c3, s)) -> true

            // Diagonal Victory
            (match(binding.a1, s) && match(binding.b2, s) && match(binding.c3, s)) ||
                    (match(binding.a3, s) && match(binding.b2, s) && match(binding.c1, s)) -> true

            else -> false
        }
    }

    /**
     * Checks if a button's text matches the specified symbol.
     */
    private fun match(button: Button, symbol: String): Boolean = button.text == symbol

    /**
     * Displays the result of the game in a dialog.
     */
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    private fun result(title: String) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val layoutDialog: View? = LayoutInflater.from(this).inflate(R.layout.result_dialog, null)
        builder.setView(layoutDialog)
        val textWin: TextView? = layoutDialog?.findViewById(R.id.textWin)
        val textNoughts: TextView? = layoutDialog?.findViewById(R.id.resultNoughts)
        val textCrosses: TextView? = layoutDialog?.findViewById(R.id.resultCrosses)
        val btnClose: Button? = layoutDialog?.findViewById(R.id.btnCancel)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)

        dialog.window?.setGravity(Gravity.CENTER)

        textWin?.text = title
        textNoughts?.text = "Noughts: $noughtsScore"
        textCrosses?.text = "Crosses: $crossesScore"

        btnClose?.setOnClickListener {
            resetBoard()
            dialog.dismiss()
        }
    }

    /**
     * Resets the game board and switches the turn to the other player.
     */
    private fun resetBoard() {

        boardList.forEach { it.text = "" }
        boardList.forEach { it.isClickable = true }
        firstTurn = if (firstTurn == Turn.NOUGHT) Turn.CROSS else Turn.NOUGHT
        currentTurn = firstTurn
        setTurnLabel()
    }

    /**
     * Checks if the game board is full (no empty cells).
     */
    private fun fullBoard(): Boolean {
        for (button in boardList) {
            if (button.text == "")
                return false
        }
        return true
    }

    /**
     * Adds a player's symbol to the game board and switches the turn to the other player.
     */
    private fun addToBoard(button: Button) {

        if (button.text != "")
            return

        if (currentTurn == Turn.NOUGHT) {
            button.text = NOUGHT
            button.isClickable = false
            currentTurn = Turn.CROSS
        } else if (currentTurn == Turn.CROSS) {
            button.text = CROSS
            button.isClickable = false
            currentTurn = Turn.NOUGHT
        }

        setTurnLabel()
    }

    /**
     * Updates the label text of the player's turn based on the current turn.
     */
    private fun setTurnLabel() {

        val turnText = when (currentTurn) {
            Turn.CROSS -> "Player turn: $CROSS"
            Turn.NOUGHT -> "Player turn: $NOUGHT"
        }
        binding.turnTV.text = turnText
    }

    /**
     * Registers the network change listener when the activity starts.
     */
    override fun onStart() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, filter)
        super.onStart()
    }

    /**
     * Unregisters the network change listener when the activity stops.
     */
    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {}

    companion object {
        const val NOUGHT = "0"
        const val CROSS = "X"
    }
}