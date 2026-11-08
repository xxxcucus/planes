
import Testing

struct VectorIteratorTests {
    
    @Test func iterate() async throws {
        let testVectorIterator = VectorIterator<Int>()
        let testVector : [Int] = [ 1, 2, 3 ]
        
        testVectorIterator.setInternalList(testVector)
        var count = 0
        while testVectorIterator.hasNext() {
            count += 1
            #expect(testVectorIterator.next() == count)
        }
    }
    
}
