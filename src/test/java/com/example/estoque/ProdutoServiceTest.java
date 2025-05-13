package com.example.estoque;

import com.example.estoque.domain.ItemPedido;
import com.example.estoque.domain.Pedido;
import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.repository.ProdutoRepository;
import com.example.estoque.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCriarProduto() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setNome("Caneta");
        produto.setQtd(10);

        when(produtoRepository.save(produto)).thenReturn(produto);

        ProdutoEntity resultado = produtoService.criarProduto(produto);

        assertEquals("Caneta", resultado.getNome());
        assertEquals(10, resultado.getQtd());
        verify(produtoRepository).save(produto);
    }

    @Test
    public void testListarProdutos() {
        ProdutoEntity produto1 = new ProdutoEntity();
        produto1.setNome("Caneta");

        ProdutoEntity produto2 = new ProdutoEntity();
        produto2.setNome("Lápis");

        List<ProdutoEntity> listaSimulada = Arrays.asList(produto1, produto2);

        when(produtoRepository.findAll()).thenReturn(listaSimulada);

        List<ProdutoEntity> resultado = produtoService.listarProdutos();

        assertEquals(2, resultado.size());
        assertEquals("Caneta", resultado.get(0).getNome());
        assertEquals("Lápis", resultado.get(1).getNome());
    }

    @Test
    public void testBuscarProdutoPorId() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Caderno");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoEntity resultado = produtoService.buscarProdutoPorId(1L);

        assertEquals("Caderno", resultado.getNome());
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void testAtualizarEstoque_ComEstoqueSuficiente() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Borracha");
        produto.setQtd(10);

        ItemPedido item = new ItemPedido();
        item.setId(1L);
        item.setQtd(4);

        Pedido pedido = new Pedido();
        pedido.setItens(List.of(item));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        produtoService.atualizarEstoque(pedido);

        assertEquals(6, produto.getQtd()); // 10 - 4
        verify(produtoRepository).save(produto);
    }

    @Test
    public void testAtualizarEstoque_ComEstoqueInsuficiente() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Caderno");
        produto.setQtd(2);

        ItemPedido item = new ItemPedido();
        item.setId(1L);
        item.setQtd(5); // Maior do que o estoque

        Pedido pedido = new Pedido();
        pedido.setItens(List.of(item));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        assertThrows(ForaDeEstoqueException.class, () -> produtoService.atualizarEstoque(pedido));

        verify(produtoRepository, never()).save(any());
    }
}
