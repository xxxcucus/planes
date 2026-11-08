

open class VectorIterator<T> {
    
    internal var m_internalList: [T]
    private var m_idx = 0

    public init() {
        m_internalList = [T]()
        reset()
    }

    open func reset() {
        m_idx = -1
    }

    open func hasNext() -> Bool {
        return m_idx < m_internalList.count - 1
    }

    open func next() -> T {
        m_idx += 1
        return m_internalList[m_idx]
    }

    open func itemNo() -> Int {
        return m_internalList.count
    }

    open func setInternalList(_ list: [T]) {
        m_internalList = list
    }
}

