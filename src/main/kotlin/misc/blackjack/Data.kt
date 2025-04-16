package misc.blackjack

import java.io.File
import java.io.FileWriter
import java.io.IOException

data class PlayerResult(
    var wins: Int = 0,
    var draws: Int = 0,
    var losses: Int = 0
){
    fun addResult(result: RoundResult) {
        when (result) {
            RoundResult.WIN -> wins++
            RoundResult.DRAW -> draws++
            RoundResult.LOSE -> losses++
        }
    }

    override fun toString(): String {
        return "$wins |  $draws |  $losses"
    }
}


class ResultTable(players: List<Player>) {
    var table : MutableMap<Player, PlayerResult> = mutableMapOf()

    fun updateResults(results: Map<Player, RoundResult>) {

        results.forEach { (player, result) ->
            // Create a new DataFrame with updated values
            if (table[player] == null) { table[player] = PlayerResult() }
            table[player]?.addResult(result)
        }


    }

    fun printStats() {
        println(" Tallying results... (Player | Wins | Draws | Losses)")
        for((key, value) in table) {
            println(" $key | $value")
        }

    }

    fun save(){
        val file = File("hello.csv")
        try{
            FileWriter(file).use{
                it.write("player,wins,draws,losses\n")
                for((key, value) in table) {
                    it.write("${key},${value.wins},${value.draws},${value.losses}\n")
                }
            }
        }catch (e: IOException){
            println("Error writing to CSV file: ${e.message}")
        }
    }


}