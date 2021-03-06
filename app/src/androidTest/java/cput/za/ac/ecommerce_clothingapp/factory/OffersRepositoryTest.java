package cput.za.ac.ecommerce_clothingapp.factory;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.Set;

import cput.za.ac.ecommerce_clothingapp.domain.Offers;
import cput.za.ac.ecommerce_clothingapp.factory.dom.Impl.OffersRepositoryImpl;
import cput.za.ac.ecommerce_clothingapp.factory.dom.OffersRepository;

/**
 * Created by Admin on 2016-05-03.
 */
public class OffersRepositoryTest extends AndroidTestCase {

    private static final String TAG = "attributes";
    private Long id;

    public void testCreateReadUpdateDelete() throws Exception {

        OffersRepository repo = new OffersRepositoryImpl(this.getContext());

        // CREATE
        Offers createEntity = new  Offers.Builder()
                .offerPrice((int)200.00)
                //.offerEndDate(AppUtil.getDate("4-May-2016"))
                //.offerStartDate(AppUtil.getDate("2-May-2016"))
                .description("Addidas Sneakers")
                .build();



        Offers insertedEntity = repo.save(createEntity);
        id=insertedEntity.getId();
        Assert.assertNotNull(TAG+" CREATE",insertedEntity);

        //READ ALL
        Set<Offers> offers= repo.findAll();
        Assert.assertTrue(TAG+" READ ALL",offers.size()>0);

        //READ ENTITY
        Offers entity = repo.findById(id);
        Assert.assertNotNull(TAG+" READ ENTITY",entity);


        //UPDATE ENTITY
        Offers  updateEntity = new Offers .Builder()
                .copy(entity)
                .description("Addidas Sneakers")
                .build();
        repo.update(updateEntity);
        Offers  newEntity = repo.findById(id);
        Assert.assertEquals(TAG+ " UPDATE ENTITY","Addidas Sneakers",newEntity.getDescription());

        // DELETE ENTITY
        repo.delete(updateEntity);
        Offers  deletedEntity = repo.findById(id);
        Assert.assertNull(TAG+" DELETE",deletedEntity);

    }
}
