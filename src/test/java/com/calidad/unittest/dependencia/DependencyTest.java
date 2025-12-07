package com.calidad.unittest.dependencia;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

//import com.calidad.unittest.dependencia.Dependency;
//import com.calidad.unittest.dependencia.SubDependency;

public class DependencyTest {
    private Dependency dependency;
    private SubDependency subDependency;

    @BeforeEach
    void setUp() throws Exception{
        //setear mock
        subDependency = mock(SubDependency.class);
        dependency = new Dependency(subDependency);
    }

    @Test 
    void testSubDependencyClassNameMock (){
        //System.out.println(subDependency.getClassName());
        
        //Resultado esperado
        String esperado = "SubDependency.class";
        when(subDependency.getClassName()).thenReturn(esperado);
        //Ejercicio
        String resultadoEjecucion = dependency.getSubdependencyClassName();

        //Verificacion
        assertThat(resultadoEjecucion,is(esperado));
    }

    /*@Test
    void testSumaDos(){
        //Resoltado esperado
        int esperado = 12;
        when(subDependency.addTwo(10)).thenReturn(12);

        //Ejercicio
        int resultadoEjecucion = subDependency.addTwo(10);

        //Verificacion
        assertThat(resultadoEjecucion, is(esperado));
    }*/

    @Test
    void testAddTwo(){
        when(subDependency.addTwo(anyInt())).thenAnswer(new Answer<Integer>(){
            public Integer answer(InvocationOnMock invoction) throws Throwable{
                int arg = (Integer) invoction.getArguments()[0];
                return arg +20;
            }
        });
        Integer resultadoEjecucion = subDependency.addTwo(100);
        assertThat(resultadoEjecucion, is(120));
    }
}
