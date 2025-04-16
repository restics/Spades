package org.example

import misc.blackjack.BlackJack
import misc.blackjack.InputPlayer
import misc.blackjack.Player

fun main(){
    println("Blackjack simulator!")
    val players: MutableList<Player> = mutableListOf()


    players.add(InputPlayer("bobbert"))
    val blackJack = BlackJack(players)

}