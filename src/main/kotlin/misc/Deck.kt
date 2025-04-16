package misc

class Card(var suit: Suit, var rank: Rank) : Comparable<Any> {
    enum class Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    enum class Rank(val value: Int) {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11)
    }

    override fun compareTo(other: Any): Int {
        return this.rank.value - (other as Card).rank.value
    }

    override fun toString(): String {
        return "${rank.name} of ${suit.name}"
    }
}
class Deck(decks: Int) {
    private val cards: MutableList<Card> = mutableListOf()

    init {
        refresh(decks)
    }

    fun refresh(decks: Int){
        cards.clear()
        for(i in 0 until decks) {
            for (suit in Card.Suit.entries) {
                for (rank in Card.Rank.entries) {
                    cards.add(Card(suit, rank))
                }
            }
        }
    }

    fun shuffle(){
        cards.shuffle()
    }

    fun draw() : Card {
        return cards.removeLast()
    }

    fun getSize() : Int{
        return cards.size
    }




}

