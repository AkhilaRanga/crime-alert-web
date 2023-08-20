import React from "react";
import "./App.css";
import Header from "./components/Header/Header";
import Content from "./components/Content/Content";
import Footer from "./components/Footer/Footer";
import { BrowserRouter } from "react-router-dom";
import { UserProvider } from "./contexts/UserContext";
import { createTheme, ThemeProvider } from "@material-ui/core/styles";
import { grey, teal } from "@material-ui/core/colors";

export const appTestId = "app-test-id";

function App() {
  // theme
  const theme = createTheme({
    palette: {
      primary: {
        main: teal[400],
      },
      secondary: {
        main: grey[400],
      },
    },
  });

  return (
    <div className="App" data-testid={appTestId}>
      <ThemeProvider theme={theme}>
        <UserProvider>
          <Header />
          <BrowserRouter>
            <Content />
          </BrowserRouter>
          <Footer />
        </UserProvider>
      </ThemeProvider>
    </div>
  );
}

export default App;
