package de.tudarmstadt.ukp.dkpro.lexsemresource.germanet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.lexsemresource.Entity;
import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource;
import de.tudarmstadt.ukp.dkpro.lexsemresource.Entity.PoS;
import de.tudarmstadt.ukp.dkpro.lexsemresource.LexicalSemanticResource.LexicalRelation;
import de.tudarmstadt.ukp.dkpro.lexsemresource.core.ResourceFactory;
import de.tudarmstadt.ukp.dkpro.lexsemresource.exception.LexicalSemanticResourceException;
import de.tudarmstadt.ukp.dkpro.lexsemresource.graph.EntityGraph;
import de.tudarmstadt.ukp.dkpro.lexsemresource.graph.EntityGraphManager;
import de.tudarmstadt.ukp.dkpro.lexsemresource.graph.EntityGraphManager.EntityGraphType;

//ae 	U+00E4
//oe 	U+00F6
//ue 	U+00FC
//AE 	U+00C4
//OE 	U+00D6
//UE 	U+00DC
//ss    U+00DF

public class GermaNetResourceTest {


	private static LexicalSemanticResource germaNet;

	@Before
	public void initializeGermaNet(){
		try{
			if (germaNet == null) {
		        germaNet = ResourceFactory.getInstance().get("germanet", "de");
            }
		} catch(Exception e){
            e.printStackTrace();
		    fail(e.getMessage());
		}
	}

    @Test
    public void testContainsLexeme() throws LexicalSemanticResourceException{
    	germaNet.setIsCaseSensitive(false);
        assertTrue(germaNet.containsLexeme("Auto"));
        assertTrue(germaNet.containsLexeme("auto"));
        assertTrue(germaNet.containsLexeme("fahren"));
        assertTrue(germaNet.containsLexeme("schnell"));
        assertFalse(germaNet.containsLexeme("grhphafah"));

        germaNet.setIsCaseSensitive(true);
        assertTrue(germaNet.containsLexeme("Auto"));
        assertFalse(germaNet.containsLexeme("aUto"));
        assertTrue(germaNet.containsLexeme("fahren"));
        assertFalse(germaNet.containsLexeme("fAhren"));
        assertTrue(germaNet.containsLexeme("schnell"));
        assertFalse(germaNet.containsLexeme("grhphafah"));
    }

    @Test
    public void testContainsEntity() throws LexicalSemanticResourceException{
    	germaNet.setIsCaseSensitive(false);
        assertTrue(germaNet.containsEntity(new Entity("Auto")));
        assertTrue(germaNet.containsEntity(new Entity("Auto",PoS.n)));
        assertFalse(germaNet.containsEntity(new Entity("Auto",PoS.v)));
        assertFalse(germaNet.containsEntity(new Entity("Auto",PoS.adj)));
        assertTrue(germaNet.containsEntity(new Entity("fahren",PoS.v)));
        assertTrue(germaNet.containsEntity(new Entity("schnell",PoS.adj)));
        assertFalse(germaNet.containsEntity(new Entity("humbelpfh")));
        assertFalse(germaNet.containsEntity(new Entity("humbelpfh",PoS.n)));
        assertFalse(germaNet.containsEntity(new Entity("humbelpfh",PoS.n, "1")));


        germaNet.setIsCaseSensitive(true);
        assertTrue(germaNet.containsEntity(new Entity("Auto")));
        assertFalse(germaNet.containsEntity(new Entity("auto")));

        assertTrue(germaNet.containsEntity(new Entity("Auto",PoS.n)));
        assertFalse(germaNet.containsEntity(new Entity("AuTo",PoS.n)));

        assertFalse(germaNet.containsEntity(new Entity("Auto",PoS.v)));
        assertFalse(germaNet.containsEntity(new Entity("Auto",PoS.adj)));
        assertTrue(germaNet.containsEntity(new Entity("fahren",PoS.v)));
        assertFalse(germaNet.containsEntity(new Entity("fAhren",PoS.v)));
        assertTrue(germaNet.containsEntity(new Entity("schnell",PoS.adj)));
        assertFalse(germaNet.containsEntity(new Entity("humbelpfh")));
        assertFalse(germaNet.containsEntity(new Entity("humbelpfh",PoS.n)));
        assertFalse(germaNet.containsEntity(new Entity("humbelpfh",PoS.n, "1")));
    }


    @Test
    public void testGetEntity() throws LexicalSemanticResourceException {

        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("Absatz#4|Abschnitt#2|Textabschnitt#1|---n");
        expectedResults.add("Absatz#2|Schuhabsatz#1|---n");
        expectedResults.add("Ablagerung#2|Absatz#6|---n");
        expectedResults.add("Absatz#5|---n");
        expectedResults.add("Absatz#3|Verkauf#1|Vertrieb#2|---n");
        expectedResults.add("Absatz#1|Schwelle#1|T\u00fcrabsatz#1|T\u00fcrschwelle#1|---n");

    	germaNet.setIsCaseSensitive(false);
    	Set<Entity> entities = germaNet.getEntity("Absatz");
        assertEquals(6, entities.size());
        for (Entity entity : entities) {
			assertTrue(entity.getId(), expectedResults.contains(entity.getId()));
		}

        germaNet.setIsCaseSensitive(true);
        entities = germaNet.getEntity("absatz");
        assertEquals(0, entities.size());

    }

    @Test
    public void testGetEntityPos() throws LexicalSemanticResourceException {
        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("Absatz#4|Abschnitt#2|Textabschnitt#1|---n");
        expectedResults.add("Absatz#2|Schuhabsatz#1|---n");
        expectedResults.add("Ablagerung#2|Absatz#6|---n");
        expectedResults.add("Absatz#5|---n");
        expectedResults.add("Absatz#3|Verkauf#1|Vertrieb#2|---n");
        expectedResults.add("Absatz#1|Schwelle#1|T\u00fcrabsatz#1|T\u00fcrschwelle#1|---n");

    	germaNet.setIsCaseSensitive(false);
    	Set<Entity> entities = germaNet.getEntity("Absatz", PoS.n);
        assertEquals(6, entities.size());
        for (Entity entity : entities) {
            assertTrue(entity.getId(), expectedResults.contains(entity.getId()));
        }

        germaNet.setIsCaseSensitive(true);
        entities = germaNet.getEntity("absatz", PoS.n);
        assertEquals(0, entities.size());
    }

    @Test
    public void testGetEntityPosSense() throws LexicalSemanticResourceException {
        Set<String> expectedResults = new HashSet<String>();
        expectedResults.add("Auto#1|Automobil#1|Kraftfahrzeug#1|Wagen#2|---n");

        // also test whether compare
        Map<String,String> autoLexemes = new HashMap<String,String>();
        autoLexemes.put("Kraftfahrzeug", "1");
        autoLexemes.put("Auto", "1");
        autoLexemes.put("Wagen", "2");
        autoLexemes.put("Automobil", "1");
        Entity autoEntity = new Entity(autoLexemes, PoS.n);

    	germaNet.setIsCaseSensitive(false);
    	Set<Entity> entities = germaNet.getEntity("Auto", PoS.n, "1");
        assertEquals(1, entities.size());
        for (Entity entity : entities) {
            assertTrue(entity.getId(), expectedResults.contains(entity.getId()));
            assertEquals(0, entity.compareTo(autoEntity));
        }


    	germaNet.setIsCaseSensitive(true);
        entities = germaNet.getEntity("auto", PoS.n, "1");
        assertEquals(0, entities.size());
    }

    @Test
    public void testGetLexicalRelations() throws LexicalSemanticResourceException {
        Set<String> expectedAntonyms = new HashSet<String>();
        expectedAntonyms.add("warm");

        Set<String> expectedSynonyms = new HashSet<String>();
        expectedSynonyms.add("hartherzig");
        expectedSynonyms.add("hart");
        expectedSynonyms.add("kaltherzig");

    	germaNet.setIsCaseSensitive(false);
    	Set<String> antonyms = germaNet.getRelatedLexemes("kalt", PoS.adj, "3", LexicalRelation.antonymy);
        assertEquals(1, antonyms.size());
        for (String antonym : antonyms) {
            assertTrue(antonym, expectedAntonyms.contains(antonym));
        }

    	germaNet.setIsCaseSensitive(true);
        antonyms = germaNet.getRelatedLexemes("kAlt", PoS.adj, "3", LexicalRelation.antonymy);
        assertEquals(0, antonyms.size());

    	germaNet.setIsCaseSensitive(false);
        Set<String> synonyms = germaNet.getRelatedLexemes("kalt", PoS.adj, "1", LexicalRelation.synonymy);
        assertEquals(3, synonyms.size());
        for (String synonym : synonyms) {
            assertTrue(synonym, expectedSynonyms.contains(synonym));
        }

    	germaNet.setIsCaseSensitive(true);
        synonyms = germaNet.getRelatedLexemes("kAlt", PoS.adj, "1", LexicalRelation.synonymy);
        assertEquals(0, synonyms.size());
    }

    @Test
	@Ignore("This test case cannot pass.  The reason is that germannet API returns the wrong " +
			"lexeme: '-kopifg', the correct lexeme should be 'kopifg'. So there may be some bugs " +
			"in the GermaNetParser.")
    public void testGetEntities() throws LexicalSemanticResourceException {
        int i=0;
    	germaNet.setIsCaseSensitive(false);
        for (Entity entity : germaNet.getEntities()) {
            Set<String> testLexemes = entity.getLexemes();
            for (String t : testLexemes) {
                System.out.println(t + " " + entity.getSense(t));
                System.out.println(entity.getPos());
            }
            assertTrue(entity.toString(), germaNet.containsEntity(entity));
            i++;
        }

        germaNet.setIsCaseSensitive(true);
        for (Entity entity : germaNet.getEntities()) {
            Set<String> testLexemes = entity.getLexemes();
            for (String t : testLexemes) {
                System.out.println(t + " " + entity.getSense(t));
                System.out.println(entity.getPos());
            }
            assertTrue(entity.toString(), germaNet.containsEntity(entity));
            i++;
        }
    }

    @Test
    public void testGetNumberOfEntities() throws LexicalSemanticResourceException {
    	germaNet.setIsCaseSensitive(false);
        assertEquals(61173, germaNet.getNumberOfEntities());


        germaNet.setIsCaseSensitive(true);
        assertEquals(61173, germaNet.getNumberOfEntities());
    }




    // There is a misspelling in the Oel-synset (Schmieroel#1 OeL#2 ---n) - it is written with an upper-case L.
    // This should be handeled by the resource.
    @Test
    public void testOelCase() throws LexicalSemanticResourceException {
        String oel = "\u00d6l";
        Set<Entity> entities = germaNet.getEntity(oel);
        for (Entity e : entities) {
            System.out.println(e);
            System.out.println(e.getSense(oel));
// at the moment, there is no way to get the correct sense with the original query term
            assertTrue(e.getSense(oel) != null);
        }
    }

//    @Ignore
    @Test
    public void testAmbiguity() throws LexicalSemanticResourceException {
        Map<String,Integer> lexemeMap = new HashMap<String,Integer>();

        germaNet.setIsCaseSensitive(false);
        for (Entity entity : germaNet.getEntities()) {
            for (String lexeme : entity.getLexemes()) {
                int count = 1;
                if (lexemeMap.containsKey(lexeme)) {
                    count = lexemeMap.get(lexeme) + 1;
                }
                lexemeMap.put(lexeme, count);
            }
        }


        int sum = 0;
        int polysemCount = 0;
        for (int count : lexemeMap.values()) {
            sum += count;
            if (count > 1) {
                polysemCount++;
            }
        }

        System.out.println("Polysemy fraction: " + (double) polysemCount / lexemeMap.size());
        System.out.println("Average polysemy: " + (double) sum / lexemeMap.size());
    }


	@Test
	public void testHyponymMap()
		throws Exception
	{
		EntityGraph eg = EntityGraphManager.getEntityGraph(germaNet, EntityGraphType.JGraphT);
		eg.getIntrinsicInformationContent(germaNet.getEntity("kalt").iterator().next());
	}
}