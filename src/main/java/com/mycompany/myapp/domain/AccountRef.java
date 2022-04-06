package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountRef.
 */
@Entity
@Table(name = "account_ref")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccountRef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "accountRef")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "accountRef" }, allowSetters = true)
    private Set<Beneficiary> beneficiaries = new HashSet<>();

    @OneToMany(mappedBy = "accountRef")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "accountRef" }, allowSetters = true)
    private Set<Balance> balances = new HashSet<>();

    @OneToMany(mappedBy = "accountRef")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "accountRef" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "accountRef")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "accountRef", "addresses" }, allowSetters = true)
    private Set<PartyId> partyIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountRef id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Beneficiary> getBeneficiaries() {
        return this.beneficiaries;
    }

    public void setBeneficiaries(Set<Beneficiary> beneficiaries) {
        if (this.beneficiaries != null) {
            this.beneficiaries.forEach(i -> i.setAccountRef(null));
        }
        if (beneficiaries != null) {
            beneficiaries.forEach(i -> i.setAccountRef(this));
        }
        this.beneficiaries = beneficiaries;
    }

    public AccountRef beneficiaries(Set<Beneficiary> beneficiaries) {
        this.setBeneficiaries(beneficiaries);
        return this;
    }

    public AccountRef addBeneficiary(Beneficiary beneficiary) {
        this.beneficiaries.add(beneficiary);
        beneficiary.setAccountRef(this);
        return this;
    }

    public AccountRef removeBeneficiary(Beneficiary beneficiary) {
        this.beneficiaries.remove(beneficiary);
        beneficiary.setAccountRef(null);
        return this;
    }

    public Set<Balance> getBalances() {
        return this.balances;
    }

    public void setBalances(Set<Balance> balances) {
        if (this.balances != null) {
            this.balances.forEach(i -> i.setAccountRef(null));
        }
        if (balances != null) {
            balances.forEach(i -> i.setAccountRef(this));
        }
        this.balances = balances;
    }

    public AccountRef balances(Set<Balance> balances) {
        this.setBalances(balances);
        return this;
    }

    public AccountRef addBalance(Balance balance) {
        this.balances.add(balance);
        balance.setAccountRef(this);
        return this;
    }

    public AccountRef removeBalance(Balance balance) {
        this.balances.remove(balance);
        balance.setAccountRef(null);
        return this;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setAccountRef(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setAccountRef(this));
        }
        this.transactions = transactions;
    }

    public AccountRef transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public AccountRef addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setAccountRef(this);
        return this;
    }

    public AccountRef removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setAccountRef(null);
        return this;
    }

    public Set<PartyId> getPartyIds() {
        return this.partyIds;
    }

    public void setPartyIds(Set<PartyId> partyIds) {
        if (this.partyIds != null) {
            this.partyIds.forEach(i -> i.setAccountRef(null));
        }
        if (partyIds != null) {
            partyIds.forEach(i -> i.setAccountRef(this));
        }
        this.partyIds = partyIds;
    }

    public AccountRef partyIds(Set<PartyId> partyIds) {
        this.setPartyIds(partyIds);
        return this;
    }

    public AccountRef addPartyId(PartyId partyId) {
        this.partyIds.add(partyId);
        partyId.setAccountRef(this);
        return this;
    }

    public AccountRef removePartyId(PartyId partyId) {
        this.partyIds.remove(partyId);
        partyId.setAccountRef(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountRef)) {
            return false;
        }
        return id != null && id.equals(((AccountRef) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountRef{" +
            "id=" + getId() +
            "}";
    }
}
