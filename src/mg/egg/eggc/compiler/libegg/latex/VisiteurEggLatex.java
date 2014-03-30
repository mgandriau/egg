package mg.egg.eggc.compiler.libegg.latex;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;

import mg.egg.eggc.compiler.libegg.base.ATTRIBUT;
import mg.egg.eggc.compiler.libegg.base.ActREGLE;
import mg.egg.eggc.compiler.libegg.base.GLOB;
import mg.egg.eggc.compiler.libegg.base.IVisiteurAction;
import mg.egg.eggc.compiler.libegg.base.IVisiteurEgg;
import mg.egg.eggc.compiler.libegg.base.NON_TERMINAL;
import mg.egg.eggc.compiler.libegg.base.REGLE;
import mg.egg.eggc.compiler.libegg.base.TDS;
import mg.egg.eggc.compiler.libegg.base.TERMINAL;
import mg.egg.eggc.runtime.libjava.EGGException;
import mg.egg.eggc.runtime.libjava.IEGGCompilationUnit;

public class VisiteurEggLatex implements IVisiteurEgg {
    // un seul visiteur d'action par visiteurEgg
    private IVisiteurAction vact;

    public IVisiteurAction getVisAction() {
        return vact;
    }

    // les attributs deja engendres
    private Vector<AttLatex> atts;

    // les terminaux deja engendres
    private Vector<TLatex> terms;

    public TLatex getTerm(String n) {
        for (Enumeration e = terms.elements(); e.hasMoreElements();) {
            TLatex tj = (TLatex) e.nextElement();
            if (tj.getNom().equals(n))
                return tj;
        }
        return null;
    }

    // les regles deja engendrées
    private Vector<RLatex> regles;

    public RLatex getRegle(int n) {
        // rajouter le test de debordement
        if (n < regles.size())
            return regles.elementAt(n);
        else
            return null;
    }

    // la grammaire
    private TDS table;

    public VisiteurEggLatex(TDS t) {
        vact = new VisiteurActionLatex();
        table = t;
        atts = new Vector<AttLatex>();
        terms = new Vector<TLatex>();
        regles = new Vector<RLatex>();
    }

    // genere main() du compilo
    // sans interet
    public void racinec() {
    }

    // genere module
    // sans interet
    public void racine() {
    }

    // genere l'analyseur lexical
    // sans interet
    public void lexical() {
    }

    // appele à la declaration du non terminal externe
    public void ex_entete(NON_TERMINAL nt) {
    }

    // genere l'entete d'un non terminal
    // appele à la creation du non terminal
    public void nt_entete(NON_TERMINAL nt) {
    }

    // appele a chaque creation de regle
    public void regle(REGLE r) {
        RLatex c = new RLatex(r);
        regles.add(c);
    }

    // appele a chaque creation de regle
    public void nt_regle(REGLE r) {
        RLatex c = getRegle(r.getNumero());
        c.setEntete();
    }

    // genere l'analyse syntaxique d'un terminal ( = accepter)
    // appele a la creation du terminal
    public void t_entete(TERMINAL t) {
        TLatex c = new TLatex(t, table);
        terms.addElement(c);
        c.setEntete();
    }

    // genere le code d'un attribut semantique
    public void nt_attribut(NON_TERMINAL nt, ATTRIBUT a) {
        // AttLatex c = getAtt(a.getNom());
        // atts.addElement(c);
        // c.setEntete();
    }

    // genere le code d'un attribut semantique
    public void nt_attribut(ATTRIBUT a) {
        AttLatex c = new AttLatex(a);
        atts.addElement(c);
        c.setEntete();
    }

    public void globale(REGLE r, GLOB g) {
        RLatex c = getRegle(r.getNumero());
        if (c == null)
            c = new RLatex(r);
        c.setGlob(g);
    }

    // genere le code d'une var globale attribut semantique
    public void nt_globale(NON_TERMINAL nt, GLOB g) {
        // NtLatex c = getNterm(nt.getNom());
        // c.setGlob(g);
    }

    public void t_attribut(TERMINAL t, ATTRIBUT a) {
    }

    // genere le code d'une action semantique
    // appele a la creation de l'action
    // nom (n), regle(r), code (c)
    public void nt_action(ActREGLE a) {
        REGLE r = a.getRegle();
        RLatex c = getRegle(r.getNumero());
        if (c == null)
            c = new RLatex(r);
        c.setAct(a);
    }

    public String car(String s) {
        return s;
    }

    public void finaliser() throws EGGException {
        StringBuffer sb = new StringBuffer();
        sb.append("%------------------ ***** -----------------------------\n");
        sb.append("%-- génération automatique de la grammaire de "
                + table.getNom() + "\n");
        sb.append("%-- au format latex\n");
        sb.append("%------------------ ***** -----------------------------\n");
        sb.append("\\documentclass[12pt, a4paper]{article}\n");
        sb.append("\\usepackage{fancyhdr}\n");
        sb.append("\\usepackage{egg}\n");
        //		sb.append("\\usepackage{multicol}\n");
//		sb.append("\\usepackage[scale={0.75,0.85}]{geometry}\n");
//		sb.append("\\usepackage{verbatim}\n");
//		sb.append("\\usepackage{graphics}\n");
//		sb.append("\\usepackage[latin1]{inputenc}\n");
//		sb.append("\\usepackage[french]{babel}\n");
//		sb.append("\\usepackage{listings}\n");
//		sb.append("\\usepackage{color}\n");
//		sb.append("\\usepackage{tabularx,verbatim}\n");
//		sb.append("\\usepackage[T1]{fontenc}\n");
sb.append("\\lhead{}\n");
        sb.append("\\chead{Grammaire de " + table.getNom() + "}\n");
        sb.append("\\rhead{}\n");
        sb.append("\\lfoot{}\n");
        sb.append("\\cfoot{}\n");
        sb.append("\\rfoot{\\hrule \\thepage}\n");
        sb.append("\\pagestyle{fancy}\n");
        //		sb.append("\\setlength{\\headrulewidth}{0pt}\n");
        //		sb.append("\\setlength{\\textheight}{650pt}\n");
        //		sb.append("\\setlength{\\columnseprule}{1pt}\n");
        sb.append("\\title{Grammaire de " + table.getNom() + " }\n");
        sb.append("\\date{\\today}\n");
        sb.append("\\begin{document}\n");
        //		sb.append("\n");
        // les attributs
        if (atts.size() > 0) {
            sb.append("\\section*{Attributs}\n");
            sb.append("\\begin{egg}\n");
            for (AttLatex a : atts) {
                sb.append(a.finaliser());
            }
            sb.append("\\end{egg}\n");
        }
        sb.append("\n");
        // les terminaux
        if (terms.size() > 0) {
            sb.append("\\section*{Terminaux}\n");
            sb.append("\\begin{egg}\n");
            for (TLatex a : terms) {
                sb.append(a.finaliser());
            }
            sb.append("\\end{egg}\n");
        }
        sb.append("\n");
        // les regles
        if (regles.size() > 0) {
            sb.append("\\section*{R\\`egles de production}\n");
            sb.append("\\begin{egg}\n");
            for (RLatex a : regles) {
                sb.append(a.finaliser());
            }
            sb.append("\\end{egg}\n");
        }
        //		sb.append("\n");
        sb.append("\\end{document}\n");

        sb.append("% Fin du fichier auto-généré\n");

        // ecriture
        IEGGCompilationUnit cu = table.getCompilationUnit();
        cu.createFile(table.getNom() + ".tex", sb.toString());

        // on copie le style dans le répertoire cible
        // repertoire de destination
        String rep = cu.getOptions().getDirectory();

        // si le style n'est pas déjà présent on le copie
        InputStream style = ClassLoader
                .getSystemResourceAsStream("latex/egg.sty");
        try {
            char[] styleContent = getInputStreamAsCharArray(style, -1, null);
            cu.createFile("egg.sty", String.copyValueOf(styleContent));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //			InputStream style = ClassLoader.getSystemResourceAsStream("mg/egg/eggc/latex/egg.sty");
        //			FileOutputStream destination = null;
        //			try {
        //				destination = new FileOutputStream(dest);
        //				byte buffer[] = new byte[512*1024];
        //				int nbLecture;
        //				while( (nbLecture = style.read(buffer)) != -1 ) {
        //                    destination.write(buffer, 0, nbLecture);
        //				}
        //			} catch (FileNotFoundException e) {
        //				System.err.println("fichier non trouvé");
        //			} catch (IOException e) {
        //				System.err.println("erreur E/S");
        //			} finally {
        //                // Quoi qu'il arrive, on ferme les flux
        //                try {
        //                        style.close();
        //                } catch(Exception e) { }
        //                try {
        //                        destination.close();
        //                } catch(Exception e) { }
        //        } 

    }

    private static char[] getInputStreamAsCharArray(InputStream stream,
            int length, String encoding) throws IOException {
        InputStreamReader reader = null;
        reader = encoding == null ? new InputStreamReader(stream)
                : new InputStreamReader(stream, encoding);
        char[] contents;
        if (length == -1) {
            contents = new char[0];
            int contentsLength = 0;
            int amountRead = -1;
            do {
                int amountRequested = Math.max(stream.available(), 8192);
                // resize contents if needed
                if (contentsLength + amountRequested > contents.length) {
                    System.arraycopy(contents, 0,
                            contents = new char[contentsLength
                                    + amountRequested], 0, contentsLength);
                }

                // read as many chars as possible
                amountRead = reader.read(contents, contentsLength,
                        amountRequested);

                if (amountRead > 0) {
                    // remember length of contents
                    contentsLength += amountRead;
                }
            } while (amountRead != -1);

            // Do not keep first character for UTF-8 BOM encoding
            int start = 0;
            if (contentsLength > 0 && "UTF-8".equals(encoding)) { //$NON-NLS-1$
                if (contents[0] == 0xFEFF) { // if BOM char then skip
                    contentsLength--;
                    start = 1;
                }
            }
            // resize contents if necessary
            if (contentsLength < contents.length) {
                System.arraycopy(contents, start,
                        contents = new char[contentsLength], 0, contentsLength);
            }
        } else {
            contents = new char[length];
            int len = 0;
            int readSize = 0;
            while ((readSize != -1) && (len != length)) {
                // See PR 1FMS89U
                // We record first the read size. In this case len is the actual
                // read size.
                len += readSize;
                readSize = reader.read(contents, len, length - len);
            }
            // Do not keep first character for UTF-8 BOM encoding
            int start = 0;
            if (length > 0 && "UTF-8".equals(encoding)) { //$NON-NLS-1$
                if (contents[0] == 0xFEFF) { // if BOM char then skip
                    len--;
                    start = 1;
                }
            }

            if (len != length)
                System.arraycopy(contents, start, (contents = new char[len]),
                        0, len);
        }
        // System.err.println(contents);
        return contents;
    }

    protected static String getOpLatex(String op) {
        return op;
    }

    protected static String getTypeLatex(String type) {
        return type;
    }

    public void m_entete(String m) {
    }

}
