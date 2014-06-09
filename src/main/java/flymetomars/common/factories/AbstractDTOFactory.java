package flymetomars.common.factories;

/**
 * This class represents the infrastructure necessary for Map<> collection
 * backed DTOFactory implementations.  That is, this abstract class gives
 * common utility types and fields for DTOFactory implementations that wish to
 * load instances per some mapping structure rather than hard-coding, etc.
 * 
 * @author Lawrence Colman
 */
/*package-private*/ abstract class AbstractDTOFactory implements DTOFactory {
    
    /**
     * Custom inner protected Mapping class to represent type-safe mappings from
     * an interface (requested by client) to an implementation (delivered to client).
     * 
     * @param <I> Type parameter for the Class of Interface that is to be mapped
     * @param <C> Type parameter for the Class of the Implementation to be used
     * 
     * @author Lawrence Colman
     */
    public final class InterfaceImplementationMapping<I,C> {
        
        private Class<I> interfaceType;
        private Class<C> concreteType;
        
        /**
         * Package private public constructor for implementing subclasses
         * 
         * @param interf Class<> object for the Interface that is to be mapped
         * @param impl ClasS<> object for the Implementation to be instantiated
         */
        /*package-private*/ InterfaceImplementationMapping(Class<I> interf,Class<C> impl) {
            this.interfaceType=interf;
            this.concreteType=impl;
        }
        
        /**
         * Property accessor for the interface Class<> type
         * @return the interface Class<> object that this mapping maps to a type
         */
        public Class getErasedInterface() {
            return this.interfaceType;
        }
        
        /**
         * Property accessor for the implementation Class<> type
         * @return the implementation Class<> type that this mapping represents
         */
        public Class<C> getSafeConcrete() {
            return this.concreteType;
        }
    }
    
}
