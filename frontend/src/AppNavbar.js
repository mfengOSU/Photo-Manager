import React, { Component } from "react";
import { Link } from "react-router-dom";
import {
  NavLink,
  Navbar,
  NavbarBrand,
  NavbarToggler,
  NavItem,
  Nav,
  Collapse,
} from "reactstrap";

class AppNavbar extends Component {
  render() {
    return (
      <Navbar color="dark" dark expand="md">
        <NavbarBrand tag={Link} to="/">
          Photos
        </NavbarBrand>
        <NavbarToggler onClick={function noRefCheck() {}} />
        <Collapse navbar>
          <Nav className="me-auto" navbar>
            <NavItem>
              <NavLink tag={Link} to="/upload">
                Upload Image
              </NavLink>
            </NavItem>
          </Nav>
        </Collapse>
      </Navbar>
    );
  }
}

export default AppNavbar;
