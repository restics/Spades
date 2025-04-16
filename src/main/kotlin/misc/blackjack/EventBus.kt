package misc.blackjack

import java.lang.reflect.Method


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class BlackJackListener


interface Observable {
    fun register(observer: Any)
    fun unregister(observer: Any)
    fun notify(event: Any)
}

class BlackJackEB : Observable {

    private val observers = mutableMapOf<Any, MutableList<Method>>()
    override fun register(observer: Any) {
        observers[observer] = observer::class.java.declaredMethods
            .filter { it.isAnnotationPresent(BlackJackListener::class.java) }
            .onEach { it.isAccessible = true }
            .toMutableList()
    }

    override fun unregister(observer: Any) {
        observers.remove(observer)
    }

    override fun notify(event: Any) {
        observers.forEach { (observer, methods) ->
            methods.forEach { method ->
                if (method.parameterTypes.firstOrNull()?.isAssignableFrom(event::class.java) == true) {
                    method.invoke(observer, event)
                }
            }
        }
    }

}