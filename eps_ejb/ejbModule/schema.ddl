
    create table Companhia (
        id bigint not null auto_increment,
        dtDelecao datetime,
        dtInsercao datetime,
        usuario_delecao varchar(50),
        usuario_insercao varchar(50),
        cnpj_cpf varchar(18) not null,
        email varchar(100) not null,
        nome varchar(100) not null,
        primary key (id)
    );

    create table Cotacao (
        id bigint not null auto_increment,
        dtDelecao datetime,
        dtInsercao datetime,
        usuario_delecao varchar(50),
        usuario_insercao varchar(50),
        codigo varchar(20),
        dtFimValidade datetime,
        dtInicioValidade datetime,
        statusCotacao varchar(255),
        fornecedor_id bigint,
        primary key (id)
    );

    create table Endereco (
        id bigint not null auto_increment,
        bairro varchar(50),
        cep varchar(9),
        complemento varchar(50),
        logradouro varchar(150),
        numero varchar(10),
        tipoEndereco varchar(255),
        companhia_id bigint,
        fornecedor_id bigint,
        municipio_id bigint,
        pais_id bigint,
        primary key (id)
    );

    create table Fornecedor (
        id bigint not null auto_increment,
        dtDelecao datetime,
        dtInsercao datetime,
        usuario_delecao varchar(50),
        usuario_insercao varchar(50),
        cnpjCpf varchar(18) not null,
        email varchar(100) not null,
        nome varchar(150) not null,
        primary key (id)
    );

    create table Municipio (
        id bigint not null auto_increment,
        codigo varchar(10) not null,
        nome varchar(50) not null,
        uf_id bigint,
        primary key (id)
    );

    create table Pais (
        id bigint not null auto_increment,
        codigo varchar(10) not null,
        nome varchar(50) not null,
        primary key (id)
    );

    create table Produto (
        id bigint not null auto_increment,
        dtDelecao datetime,
        dtInsercao datetime,
        usuario_delecao varchar(50),
        usuario_insercao varchar(50),
        codigo varchar(20) not null,
        descricao varchar(240) not null,
        nome varchar(50) not null,
        unidade_medida_id bigint,
        primary key (id)
    );

    create table Telefone (
        id bigint not null auto_increment,
        numero varchar(15) not null,
        tipoTelefone varchar(255),
        companhia_id bigint,
        fornecedor_id bigint,
        primary key (id)
    );

    create table Uf (
        id bigint not null auto_increment,
        codigo varchar(10) not null,
        nome varchar(50) not null,
        pais_id bigint,
        primary key (id)
    );

    create table Usuario (
        user_type varchar(3) not null,
        id bigint not null auto_increment,
        dtDelecao datetime,
        dtInsercao datetime,
        usuario_delecao varchar(50),
        usuario_insercao varchar(50),
        email varchar(100) not null,
        qtd_tentativas integer,
        role varchar(255),
        senha varchar(50) not null,
        senha_inicial varchar(50) not null,
        senhas_antigas varchar(200),
        statusUsuario varchar(255),
        fornecedor_id bigint,
        companhia_id bigint,
        primary key (id)
    );

    create table item_cotacao (
        id bigint not null auto_increment,
        qtd_produto double precision not null,
        valor_total decimal(10,4),
        cotacao_id bigint,
        produto_id bigint,
        primary key (id)
    );

    create table log_cotacao (
        id bigint not null auto_increment,
        descricao varchar(240) not null,
        dtInsercao datetime,
        cotacao_id bigint,
        primary key (id)
    );

    create table produto_fornecedor (
        produto_id bigint not null,
        fornecedor_id bigint not null,
        primary key (produto_id, fornecedor_id)
    );

    create table unidade_medida (
        id bigint not null auto_increment,
        nome varchar(50) not null,
        sigla varchar(10) not null,
        primary key (id)
    );

    alter table Cotacao 
        add index FK9C3BAD38D186B1D0 (fornecedor_id), 
        add constraint FK9C3BAD38D186B1D0 
        foreign key (fornecedor_id) 
        references Fornecedor (id);

    alter table Endereco 
        add index FK6B07CBE912831690 (pais_id), 
        add constraint FK6B07CBE912831690 
        foreign key (pais_id) 
        references Pais (id);

    alter table Endereco 
        add index FK6B07CBE93C6F1E44 (companhia_id), 
        add constraint FK6B07CBE93C6F1E44 
        foreign key (companhia_id) 
        references Companhia (id);

    alter table Endereco 
        add index FK6B07CBE9D186B1D0 (fornecedor_id), 
        add constraint FK6B07CBE9D186B1D0 
        foreign key (fornecedor_id) 
        references Fornecedor (id);

    alter table Endereco 
        add index FK6B07CBE9251839A4 (municipio_id), 
        add constraint FK6B07CBE9251839A4 
        foreign key (municipio_id) 
        references Municipio (id);

    alter table Municipio 
        add index FK863DB7CD6B642850 (uf_id), 
        add constraint FK863DB7CD6B642850 
        foreign key (uf_id) 
        references Uf (id);

    alter table Produto 
        add index FK50C666D9F8123181 (unidade_medida_id), 
        add constraint FK50C666D9F8123181 
        foreign key (unidade_medida_id) 
        references unidade_medida (id);

    alter table Telefone 
        add index FKB2C2CF0A3C6F1E44 (companhia_id), 
        add constraint FKB2C2CF0A3C6F1E44 
        foreign key (companhia_id) 
        references Companhia (id);

    alter table Telefone 
        add index FKB2C2CF0AD186B1D0 (fornecedor_id), 
        add constraint FKB2C2CF0AD186B1D0 
        foreign key (fornecedor_id) 
        references Fornecedor (id);

    alter table Uf 
        add index FKAB112831690 (pais_id), 
        add constraint FKAB112831690 
        foreign key (pais_id) 
        references Pais (id);

    alter table Usuario 
        add index FK5B4D8B0E3C6F1E44 (companhia_id), 
        add constraint FK5B4D8B0E3C6F1E44 
        foreign key (companhia_id) 
        references Companhia (id);

    alter table Usuario 
        add index FK5B4D8B0ED186B1D0 (fornecedor_id), 
        add constraint FK5B4D8B0ED186B1D0 
        foreign key (fornecedor_id) 
        references Fornecedor (id);

    alter table item_cotacao 
        add index FKE72D742CB6B188A4 (produto_id), 
        add constraint FKE72D742CB6B188A4 
        foreign key (produto_id) 
        references Produto (id);

    alter table item_cotacao 
        add index FKE72D742C268EF844 (cotacao_id), 
        add constraint FKE72D742C268EF844 
        foreign key (cotacao_id) 
        references Cotacao (id);

    alter table log_cotacao 
        add index FK296EDB5D268EF844 (cotacao_id), 
        add constraint FK296EDB5D268EF844 
        foreign key (cotacao_id) 
        references Cotacao (id);

    alter table produto_fornecedor 
        add index FK62C426BB6B188A4 (produto_id), 
        add constraint FK62C426BB6B188A4 
        foreign key (produto_id) 
        references Produto (id);

    alter table produto_fornecedor 
        add index FK62C426BD186B1D0 (fornecedor_id), 
        add constraint FK62C426BD186B1D0 
        foreign key (fornecedor_id) 
        references Fornecedor (id);
