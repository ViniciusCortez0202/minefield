package estudo.model

enum class  FieldEvent{
    OPEN,
    FLAG,
    NOFLAG,
    EXPLOSION,
    RESTART
}

data class Field(val row: Int, val col: Int){
    private val neighbors = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, FieldEvent) -> Unit>()

    var flag: Boolean = false
    var open: Boolean = false
    var mine: Boolean = false

    val noFlag: Boolean get() = !flag
    val close: Boolean get() = !open
    val safe: Boolean get() = !mine
    val goalAttain: Boolean get() = safe && open || mine && flag
    val quantityNeighborWithMine: Int get() = neighbors.filter { it.mine }.size
    val neighborhoodSafe: Boolean get() = neighbors.map{ it.safe }.reduce{ value, safe -> value && safe }

    fun addNeighbor(neighbor: Field){
        neighbors.add(neighbor)
    }

    fun onEvent(callback: (Field, FieldEvent) -> Unit){
        callbacks.add(callback)
    }

    fun open(){
        if(close){
            open = true
            if(mine){
                callbacks.forEach{
                    it(this, FieldEvent.EXPLOSION)
                }
            } else {
                callbacks.forEach{
                    it(this, FieldEvent.OPEN)
                }
                neighbors.filter { it.close && it.safe && neighborhoodSafe }.forEach { it.open() }
            }
        }
    }

    fun changeFlag(){
        if(close){
            flag = !flag
            val event = if(flag) FieldEvent.FLAG else FieldEvent.NOFLAG
            callbacks.forEach { it(this, event) }
        }
    }

    fun addMine() {
        mine = true
    }

    fun restart(){
        open = false
        mine = false
        flag = false
        callbacks.forEach { it(this, FieldEvent.RESTART) }
    }

}
