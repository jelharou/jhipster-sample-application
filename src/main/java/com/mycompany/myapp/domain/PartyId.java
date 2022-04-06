package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PartyId.
 */
@Entity
@Table(name = "party_id")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PartyId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "accountRA", "beneficiaries", "balances", "transactions", "partyIds" }, allowSetters = true)
    private AccountRef accountRef;

    @JsonIgnoreProperties(value = { "partyId" }, allowSetters = true)
    @OneToOne(mappedBy = "partyId")
    private Party party;

    @OneToMany(mappedBy = "partyId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "partyId" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartyId id(Long id) {
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

    public PartyId accountRef(AccountRef accountRef) {
        this.setAccountRef(accountRef);
        return this;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(Party party) {
        if (this.party != null) {
            this.party.setPartyId(null);
        }
        if (party != null) {
            party.setPartyId(this);
        }
        this.party = party;
    }

    public PartyId party(Party party) {
        this.setParty(party);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setPartyId(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setPartyId(this));
        }
        this.addresses = addresses;
    }

    public PartyId addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public PartyId addAddress(Address address) {
        this.addresses.add(address);
        address.setPartyId(this);
        return this;
    }

    public PartyId removeAddress(Address address) {
        this.addresses.remove(address);
        address.setPartyId(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartyId)) {
            return false;
        }
        return id != null && id.equals(((PartyId) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartyId{" +
            "id=" + getId() +
            "}";
    }
}
