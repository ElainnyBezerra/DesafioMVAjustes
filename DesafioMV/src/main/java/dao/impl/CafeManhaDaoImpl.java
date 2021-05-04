package dao.impl;

import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import dao.CafeManhaDao;
import dao.PessoaDao;
import entidade.CafeManha;
import entidade.Pessoa;
import util.JpaUtil;

public class CafeManhaDaoImpl implements CafeManhaDao {

	private EntityManager em;
	private EntityTransaction et;

	@Override
	public void inserir(CafeManha cafeManha) {

		Scanner cmInt = new Scanner(System.in);
		Scanner cmString = new Scanner(System.in);
		int resp1 = 0;
		String opcao;

		try {
			this.em = JpaUtil.getEntityManager();
			et = em.getTransaction();
			et.begin();
			em.createNativeQuery("INSERT INTO CAFEMANHA (OPCAO, CPF_PESSOA) VALUES (?,?)")
					.setParameter(1, cafeManha.getOpcao()).setParameter(2, cafeManha.getPessoa().getCpf())
					.executeUpdate();
			System.out.println("Opção de Café da manhã inserida com Sucesso!!");
			System.out.println("Deseja inserir mais 1 Opção ?(1 - Sim / 2 - Não)");
			resp1 = cmInt.nextInt();

			while (resp1 == 1) {
				CafeManha cf = new CafeManha();
				System.out.println("Informe a Opção : ");
				opcao = cmString.nextLine();
				cf.setOpcao(opcao);

				em.createNativeQuery("INSERT INTO CAFEMANHA (OPCAO, CPF_PESSOA) VALUES (?,?)")
						.setParameter(1, cf.getOpcao()).setParameter(2, cafeManha.getPessoa().getCpf()).executeUpdate();
				System.out.println("Opção de Café da manhã inserida com Sucesso!!");
				System.out.println("Deseja inserir mais 1 Opção ?(1 - Sim / 2 - Não)");
				resp1 = cmInt.nextInt();
			}

			et.commit();
		} catch (Exception e) {
			if (em.isOpen()) {
				et.rollback();
			}
			System.out.println("Erro transação");
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}

	}

	@Override
	public void atualizar(CafeManha cafeManha) {

		try {
			this.em = JpaUtil.getEntityManager();
			et = em.getTransaction();
			et.begin();
			em.createNativeQuery("UPDATE CAFEMANHA SET CPF_PESSOA = ? WHERE OPCAO = ?")
					.setParameter(2, cafeManha.getOpcao()).setParameter(1, cafeManha.getPessoa().getCpf())
					.executeUpdate();
			et.commit();
		} catch (Exception e) {
			if (em.isOpen()) {
				et.rollback();
			}
			System.out.println("Erro transação");
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}

	}

	@Override
	public void remover(String opcao) {

		try {
			this.em = JpaUtil.getEntityManager();
			et = em.getTransaction();
			et.begin();
			em.createNativeQuery(
					"DELETE FROM CAFEMANHA WHERE COD_OPCAO = (SELECT COD_OPCAO FROM CAFEMANHA WHERE OPCAO = ?)")
					.setParameter(1, opcao).executeUpdate();
			et.commit();

		} catch (Exception e) {
			if (em.isOpen()) {
				et.rollback();
			}
			System.out.println("Erro transação");
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}

	}

	@Override
	public CafeManha consultar(String opcao) {

		CafeManha cafeManha = new CafeManha();

		try {
			this.em = JpaUtil.getEntityManager();
			cafeManha = em.find(CafeManha.class, opcao);
		} catch (Exception e) {
			if (em.isOpen()) {
				et.rollback();
			}
			System.out.println("Erro transação");
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
		return cafeManha;
	}

	@Override
	public List<CafeManha> listarTodos() {

		this.em = JpaUtil.getEntityManager();
		Query query = em.createQuery("from CafeManha e");
		List<CafeManha> listaCafeManha = query.getResultList();
		em.close();
		return listaCafeManha;
	}

}
