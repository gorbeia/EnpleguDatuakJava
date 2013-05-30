
package com.aiaraldea.gizartesegurantzawriter.ss.web;

import com.aiaraldea.gizartesegurantzawriter.ss.SSLastDayOfMonthByCouncilParser;
import java.util.Collection;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author inaki
 */
public class ListLinksTest {
    
    /**
     * Test of fetch method, of class ListLinks.
     */
    @Ignore
    @Test
    public void testFetchLinks() throws Exception {
        System.out.println("fetch");
        String url = "http://www.seg-social.es/Internet_1/Estadistica/Est/AfiliacionAltaTrabajadores/AfiliacionesAltaLaboral/Afiliaci_n__ltimo_d_a_del_mes/AfiliacionMunicipios/index.htm";
        Collection<String> urls = ListLinks.fetchLinks(url);
        assert urls.size() > 0;
    }
    
//    public void testFetchFiles() throws Exception {
//        Collection<String> urls= new ArrayList<>();
//        urls.add("http://www.seg-social.es/prdi00/idcplg?IdcService=GET_FILE&dID=138150&dDocName=176192&allowInterrupt=1");
//        ListLinks.fetchFiles(urls);
//    }
//    
//    public void testFetchAllFiles() throws Exception {
//        String url = "http://www.seg-social.es/Internet_1/Estadistica/Est/AfiliacionAltaTrabajadores/AfiliacionesAltaLaboral/Afiliaci_n__ltimo_d_a_del_mes/AfiliacionMunicipios/index.htm";
//        Collection<String> urls = ListLinks.fetchLinks(url);
//        ListLinks.fetchFiles(urls);
//    }
    @Ignore
    @Test
    public void testFetchUnproccessedLinks() throws Exception {
        String url = "http://www.seg-social.es/Internet_1/Estadistica/Est/AfiliacionAltaTrabajadores/AfiliacionesAltaLaboral/Afiliaci_n__ltimo_d_a_del_mes/AfiliacionMunicipios/index.htm";
        Collection<String> urls = ListLinks.fetchLinks(url);
        
        SSLastDayOfMonthByCouncilParser parser = new SSLastDayOfMonthByCouncilParser();
        ListLinks.fetchUnfetchedFiles(urls, parser);
    }
    
}
