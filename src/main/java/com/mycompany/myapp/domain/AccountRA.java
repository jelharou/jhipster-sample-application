package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountRA.
 */
@Entity
@Table(name = "account_ra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccountRA implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "accountRA", "beneficiaries", "balances", "transactions", "partyIds" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private AccountRef accountRef;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountRA id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountRef getAccountRef() {
        return this.accountRef;
    }

    public void setAccountRef(AccountRef accountRef) {
        this.accountRef = accountRef;
    }

    public AccountRA accountRef(AccountRef accountRef) {
        this.setAccountRef(accountRef);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountRA)) {
            return false;
        }
        return id != null && id.equals(((AccountRA) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountRA{" +
            "id=" + getId() +
            "}";
    }
}
