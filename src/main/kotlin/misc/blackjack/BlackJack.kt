package misc.blackjack


import misc.Card
import misc.Deck


class BlackJack(
    val players: MutableList<Player> = mutableListOf(),
    val numDecks: Int = 2,
    val shuffleThreshold :Int = 30,
    val numRounds: Int = 10
){

    val dealer: Dealer = Dealer()
    val deck: Deck = Deck(numDecks)
    val eventBus: BlackJackEB = BlackJackEB()

    val results = ResultTable(players)
    init{
        reset(true)
        var loop: Boolean = true

        players.forEach{player ->
            eventBus.register(player)
        }
        autoRounds(numRounds)
        results.printStats()
    }
    fun autoRounds(n : Int){
        reset(true)
        players.forEach{player ->
            eventBus.register(player)

        }
        for(i in 0 until n) {
            results.updateResults(round())

        }
    }

    fun reset(reshuffle: Boolean = false){
        if (reshuffle){
            println("Shuffling deck... ")
            eventBus.notify(ShuffledDeckEvent())
            deck.refresh(numDecks)
            deck.shuffle()
        }
        dealer.discard()
        players.forEach{player -> player.discard()}
    }
    fun round() : MutableMap<Player, RoundResult>{
        println("Starting new round... ")
        reset(deck.getSize() < shuffleThreshold)
        val shown = dealPlayer(dealer)
        players.forEach{player -> dealPlayer(player)}
        dealPlayer(dealer)
        players.forEach{player -> dealPlayer(player)}

        var roundLoop = dealer.getHandValue() != 21
        for (player in players) {
            while (roundLoop){
                println("$player has hand: ${player.hand} with value ${player.getHandValue()}")
                when (player.decision(players.flatMap{it.hand}, shown)) {
                    Decision.HIT -> {
                        val card = dealPlayer(player)
                        println("Dealt a $card to $player")
                        if (player.getHandValue() > 21){
                            println("Player $player busted!")

                            break;
                        }
                    }

                    Decision.STAND -> {
                        println("$player stands.")
                        roundLoop = false
                    }

                    else -> {
                        println("Unrecognised response.")
                    }
                }
            }
            roundLoop = true;
        }

        println("Draw round over.")
        println("Dealer reveals his hand: ${dealer.hand}")
        roundLoop = true
        while (roundLoop){
            when(dealer.decision(listOf(), shown)){

                Decision.HIT -> {
                    val card = dealPlayer(dealer)
                    println("Dealer draws a $card with hand value ${dealer.hand}")
                    if (dealer.getHandValue() > 21){
                        println("Dealer busts!")

                    }
                }
                Decision.STAND -> {
                    roundLoop = false
                }
                else ->{
                    println("error, dealer should not do that")

                }
            }


        }

        println("Dealer is finished drawing!")
        println("Finishing round!")
        println("Dealer had: ${dealer.hand} totalling to ${dealer.getHandValue()}")

        val results : MutableMap<Player, RoundResult> = mutableMapOf()
        for(player in players){
            println("Player $player had: ${player.hand} totalling to ${player.getHandValue()}")
            var result: RoundResult = RoundResult.LOSE
            if (22 > dealer.getHandValue() && dealer.getHandValue() > player.getHandValue()){

                println("Player $player loses!")
            }
            else if (22 > dealer.getHandValue() && dealer.getHandValue() == player.getHandValue()){
                result = RoundResult.DRAW
                println("Player $player draws!")
            }

            else if (22 > player.getHandValue() && player.getHandValue() > dealer.getHandValue()){
                result = RoundResult.WIN
                println("Player $player Wins!")
            }
            else if (dealer.getHandValue() > 21 && player.getHandValue() < 21){
                result = RoundResult.WIN
                println("Player $player Wins!")
            }
            else {
                println("Player $player loses!")
            }
            results[player] = result
        }


        return results
    }

    private fun dealPlayer(player : Player) : Card {
        val card = deck.draw()

        player.draw(card)
        return card
    }

}


fun main(){
    println("Blackjack simulator!")
    val players: MutableList<Player> = mutableListOf()

    for (i in 0 until 5){
        players.add(RandomPlayer(i))
    }
    val blackJack = BlackJack(players)

}