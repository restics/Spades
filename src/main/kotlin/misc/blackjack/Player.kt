package misc.blackjack

import misc.Card
import kotlin.random.Random

open class Player(var name: String = ""){
    var hand: MutableList<Card> = mutableListOf()

    fun getHandValue() : Int {
        var handValue = 0

        for (card in hand){

            if (card.rank == Card.Rank.ACE){
                handValue += if(handValue + 11 > 21) 1 else 11
            }
            else{
                handValue += card.rank.value
            }
        }
        return handValue
    }

    fun draw(card: Card){
        hand.add(card)
        hand.sort()
    }

    fun discard(){
        hand.clear()
    }

    fun double(){

    }
    override fun toString(): String {
        return name
    }

    // called when the game asks for the player's move
    open fun decision(playerCards: List<Card>, shown: Card): Decision {
        return Decision.STAND
    }
}

class Dealer : Player("dealer") {

    override fun decision(playerCards: List<Card>, shown: Card) : Decision {
        return if (getHandValue() < 17) Decision.HIT else Decision.STAND
    }

}

class CardCounter(i: Int) : Player(i.toString()) {
    var count = 0;
    override fun decision(playerCards: List<Card>, shown: Card) : Decision {
        return if (getHandValue() < 17) Decision.HIT else Decision.STAND
    }

}

class RandomPlayer(i: Int) : Player(i.toString()) {
    var count = 0;
    override fun decision(playerCards: List<Card>, shown: Card) : Decision {
        return if (Random.nextInt(0,2) == 0) Decision.HIT else Decision.STAND
    }
}

class InputPlayer(i: String) : Player(i.toString()) {
    override fun decision(playerCards: List<Card>, shown: Card) : Decision {
        println("Dealer has the card $shown.")
        println("You have the cards $hand with card value ${getHandValue()}.")
        println("Type h to hit and s to stand.")
        val response: String = readln()
        when (response){
            "h" ->{
                return Decision.HIT
            }
            "s" ->{
                return Decision.STAND
            }
            else ->{
                return Decision.STAND
            }
        }
    }
}

