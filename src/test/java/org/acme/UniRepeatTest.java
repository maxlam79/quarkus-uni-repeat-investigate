package org.acme;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@QuarkusTest
public class UniRepeatTest {

    @Test
    void testUniRepeatUntil( ) {
        AtomicInteger counter = new AtomicInteger( 0 );

        Uni< Integer > numIncreateUni = Uni.createFrom( ).item( counter ).map( c -> {
            int num = counter.incrementAndGet( );
            Log.info( "num: " + num );

            return num;
        } );

        List< Integer > numResultList = numIncreateUni.repeat( ).until( num -> {
            // It should run numIncreateUni 2 times.
            return ( num == 2 );
        } ).collect( ).asList( ).await( ).indefinitely( );

        // The expected result in the list should be [1, 2]
        Log.info( "numResultList size: " + numResultList.size( ) );

        for ( int i = 0; i < numResultList.size( ); ++i ) {
            Log.info( "numList[" + i + "]: " + numResultList.get( i ) );
        }

        // But turns out that only [1] was "collected"...where is [2]?
        Assertions.assertEquals( 2, numResultList.size( ) );
    }

    @Test
    void testUniRepeatWhilst( ) {
        AtomicInteger counter = new AtomicInteger( 0 );

        Uni< Integer > numIncreateUni = Uni.createFrom( ).item( counter ).map( c -> {
            int num = counter.incrementAndGet( );
            Log.info( "num: " + num );

            return num;
        } );

        List< Integer > numResultList = numIncreateUni.repeat( ).whilst( num -> {
            // It should run numIncreateUni 2 times.
            return ( num == 2 );
        } ).collect( ).asList( ).await( ).indefinitely( );

        // The expected result in the list should be [1, 2]
        Log.info( "numResultList size: " + numResultList.size( ) );

        for ( int i = 0; i < numResultList.size( ); ++i ) {
            Log.info( "numList[" + i + "]: " + numResultList.get( i ) );
        }

        // But turns out that only [1] was "collected"...where is [2]?
        Assertions.assertEquals( 2, numResultList.size( ) );
    }
}
